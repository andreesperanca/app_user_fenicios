package com.voltaire.fenicios.ui_innerApp.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.NavHostFragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mercadopago.android.px.core.MercadoPagoCheckout
import com.mercadopago.android.px.model.Payment
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.voltaire.fenicios.MainActivity
import com.voltaire.fenicios.R
import com.voltaire.fenicios.databinding.ActivityStartPaymentBinding
import com.voltaire.fenicios.model.*
import com.voltaire.fenicios.ui_innerApp.requests.RequestFragment
import com.voltaire.fenicios.utils.Constants
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class StartPaymentActivity : AppCompatActivity() {

    private lateinit var cartUser: MutableList<Product>

    private lateinit var listRequest : MutableList<Purchase>

    private lateinit var binding: ActivityStartPaymentBinding
    private val REQUEST_CODE: Int = 210
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var cUserAddress: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val args = intent.getFloatExtra("cartPrice", 0.00f)
        binding.purchaseValue.text = getString(R.string.cartValue, args.toString())
        loadAddress()
    }

    override fun onStart() {
        super.onStart()

        val args = intent.getFloatExtra("cartPrice", 0.00f)

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
                itemJSON.put("unit_price", args)
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

            val requestQueue = Volley.newRequestQueue(this)
            val PUBLIC_KEY_SANDBOX = Constants.PUBLIC_KEY_SANDBOX
            val accessSandBoxToken = Constants.ACCESS_SANDBOX_TOKEN
            val url = "${Constants._URL}${accessSandBoxToken}"

            val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, url, jsonObject,
                Response.Listener { response ->
                    try {
                        Log.i("debinf MainAct", "response JSONObject: $response")
                        val checkoutPreferenceId = response.getString("id")
                        MercadoPagoCheckout.Builder(PUBLIC_KEY_SANDBOX, checkoutPreferenceId)
                            .build()
                            .startPayment(this, REQUEST_CODE)

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
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadAddress() {
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                        val cUser = it.result.toObject(User::class.java)
                        if (cUser?.address != null) {
                        cartUser = cUser?.cart!!
                        cUserAddress = cUser?.address!!

                        with(binding) {
                            purchaseDistrict.setText(cUserAddress.district)
                            purchaseStreet.setText(cUserAddress.street)
                            purchaseHouseNumber.setText(cUserAddress.number)
                        }
                    }
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                val payment: Payment? =
                    data!!.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT) as Payment?

                Toast.makeText(this, "Pagamento finalizado, pedido adicionado na aba: Pedidos.", Toast.LENGTH_LONG).show()

                val newPurchase = Purchase(
                    payment?.transactionAmount.toString(),
                    cUserAddress,
                    payment?.payer.toString(),
                    cartUser,
                    payment?.paymentStatus.toString(),
                    StatusRequest.PENDENTE
                )

                val date = Calendar.getInstance().time.toLocaleString()

                db.collection("pedidos")
                    .document("pendentes")
                    .collection(auth.currentUser?.uid!!)
                    .document(date.toString())
                    .set(newPurchase)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .update("cart", emptyArray<Product>().toMutableList())

            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                if (data != null && data.extras != null && data.extras!!.containsKey(
                        MercadoPagoCheckout.EXTRA_ERROR
                    )
                ) {
                    val mercadoPagoError =
                        data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR) as MercadoPagoError?
                    Toast.makeText(this, mercadoPagoError!!.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Pagamento cancelado.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}