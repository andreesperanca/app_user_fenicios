package com.voltaire.fenicios.ui_innerApp.purchase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mercadopago.android.px.core.MercadoPagoCheckout
import com.mercadopago.android.px.model.Payment
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.voltaire.fenicios.MainActivity
import com.voltaire.fenicios.databinding.FragmentPurchaseBinding
import com.voltaire.fenicios.model.Purchase
import com.voltaire.fenicios.ui_innerApp.productsdetails.ProductDetailsFragmentArgs
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PurchaseFragment : Fragment() {

    private lateinit var binding : FragmentPurchaseBinding

    private val REQUEST_CODE: Int = 210

    private val args: PurchaseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPurchaseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val purchasePrice = args.valuePurchase

        with(binding) {

            purchaseValue.text = purchasePrice.toString()
            val cUser = (context as MainActivity).userLoggedReal

            if ( cUser?.address != null) {
                purchaseDistrict.setText(cUser.address.district)
                purchaseStreet.setText(cUser.address.street)
                purchaseHouseNumber.setText(cUser.address.number)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.btnConfirm.setOnClickListener {
            val jsonObject = JSONObject()
            val itemJSON = JSONObject()
            val payerJSON = JSONObject()
            val itemJsonArray = JSONArray()

            try {
                //ITEM
                itemJSON.put("title", "Pizza")
                itemJSON.put("quantity", 1)
                itemJSON.put("currency_id", "BRL")
                itemJSON.put("unit_price", args.valuePurchase)
                itemJsonArray.put(itemJSON)

                //PHONE
                val phoneJSON = JSONObject()
                phoneJSON.put("number", "880.402.7555")

                //IDJSON
                val idJSON = JSONObject()
                idJSON.put("type", "CPF")
                idJSON.put("number", "12345678909")

                //ADDRESSJSON
                val addressJSON = JSONObject()
                addressJSON.put("street_name", "APRO")
                addressJSON.put("street_number", 25598)
                addressJSON.put("zip_code", "8972")

                //PAYERJSON
                payerJSON.put("name", "APRO")
                payerJSON.put("email", "test@gmail.com")
                payerJSON.put("date_created", "2015-06-02T12:58:41.425-04:00")
                payerJSON.put("phone", phoneJSON)
                payerJSON.put("identification", idJSON)
                payerJSON.put("address", addressJSON)

                //JSON OBJECT

                jsonObject.put("items", itemJsonArray)
                jsonObject.put("payer", payerJSON)


            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val userJson = JSONObject()
            userJson.put("user", payerJSON)

            val requestQueue = Volley.newRequestQueue(requireContext())
            val PUBLIC_KEY_SANDBOX = "TEST-fa840a8a-0df1-4995-830c-a81ba5b9b4d1"
            val accessSandBoxToken = "TEST-4589497852166609-042514-bb3c4c6085b5dd3df1e947d3c3f9a29c-1001599393"
            val url = "https://api.mercadopago.com/checkout/preferences?access_token=${accessSandBoxToken}"

            val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, url, jsonObject,
                Response.Listener { response ->
                    try {
                        Log.i("debinf MainAct", "response JSONObject: $response")
                        val checkoutPreferenceId = response.getString("id")
                        MercadoPagoCheckout.Builder(PUBLIC_KEY_SANDBOX, checkoutPreferenceId)
                            .build()
                            .startPayment(requireContext(), REQUEST_CODE)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },

                Response.ErrorListener { error ->
                    Log.i(
                        "debinf MainAct",
                        "response ERROR: " + error.networkResponse.allHeaders
                    )
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            requestQueue.add(jsonObjectRequest)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                val payment : Payment? =
                    data!!.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT) as Payment?
                Toast.makeText(requireContext(), payment!!.paymentStatus, Toast.LENGTH_LONG).show()

            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                if (data != null && data.extras != null && data.extras!!.containsKey(
                        MercadoPagoCheckout.EXTRA_ERROR
                    )
                ) {
                    val mercadoPagoError =
                        data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR) as MercadoPagoError?
                    Toast.makeText(requireContext(), mercadoPagoError!!.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "pagamento cancelado", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
