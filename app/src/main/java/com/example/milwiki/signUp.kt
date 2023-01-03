package com.example.milwiki

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.Volley
import com.example.milwiki.ui.login.LoginFragment
import org.json.JSONException
import org.json.JSONObject


class signUp : AppCompatActivity() {
    private lateinit var  et_id : EditText
    private lateinit var et_pass : EditText
    private lateinit var et_name : EditText
    private lateinit var et_age : EditText
    private lateinit var et_hak : EditText
    private lateinit var et_major : EditText
    private lateinit var et_passck : EditText
    private lateinit var validateButton: Button
    private var validate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //아이디 값 찾아주기
        et_id=findViewById(R.id.et_id);
        et_pass=findViewById(R.id.et_pass);
        et_name=findViewById(R.id.et_name);
        et_age=findViewById(R.id.et_age);
        et_hak=findViewById(R.id.et_hak);
        et_major=findViewById(R.id.et_maj);
        et_passck=findViewById(R.id.et_passck);
        validateButton=findViewById(R.id.validateButton);
        validateButton.setOnClickListener(object : View.OnClickListener{
            // id중복체크
            override fun onClick(view : View) {
                var userID : String = et_id.getText().toString()
                if (validate){
                    return
                }
                if(userID == "") {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@signUp)
                    val dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다")
                        .setPositiveButton("확인", null)
                        .create()
                    dialog.show()
                    return
                }
                var responseListener : Response.Listener<String> = object : Response.Listener<String> {
                    override fun onResponse(response: String) {
                        try {
                            val jsonResponse = JSONObject(response)
                            val success = jsonResponse.getBoolean("success")
                            if (success) {
                                val builder = AlertDialog.Builder(this@signUp)
                                val dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                    .setPositiveButton("확인", null)
                                    .create()
                                dialog.show()
                                et_id.isEnabled = false
                                validate = true
                                validateButton.text = "확인"
                            } else {
                                val builder = AlertDialog.Builder(this@signUp)
                                val dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                    .setNegativeButton("확인", null)
                                    .create()
                                dialog.show()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
                val validateRequest : ValidateRequest = ValidateRequest(userID, responseListener)
                val queue : RequestQueue = Volley.newRequestQueue(this@signUp)
                queue.add(validateRequest)
            }
        })
        val btn_register : Button = findViewById(R.id.btn_register)
        btn_register.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v : View?) {
                //editText에 있는 값을 가져온다
                val userID : String = et_id.getText().toString()
                val userPass = et_pass.text.toString()
                val userName = et_name.text.toString()
                val userAge = et_age.text.toString().toInt()
                val userHak = et_hak.text.toString().toInt()
                val userMajor = et_major.text.toString()
                val PassCk = et_passck.text.toString()

                val responseListener: Listener<String> = Listener { response ->
                    //volley
                    try {
                        val jasonObject = JSONObject(response) //Register2 php에 response
                        val success = jasonObject.getBoolean("success") //Register2 php에 sucess
                        if (userPass == PassCk) {
                            if (success) { //회원등록 성공한 경우
                                Toast.makeText(this@signUp, "회원 등록 성공", Toast.LENGTH_SHORT).show()
                                val intent =
                                    Intent(this@signUp, LoginFragment::class.java)
                                startActivity(intent)
                            }
                        } else { //회원등록 실패한 경우
                            Toast.makeText(applicationContext, "회원 등록 실패", Toast.LENGTH_SHORT)
                                .show()
                            return@Listener
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                val registerRequest : RegisterRequest = RegisterRequest(
                    userID,
                    userPass,
                    userName,
                    userAge,
                    userHak,
                    userMajor,
                    responseListener
                )
                val queue : RequestQueue = Volley.newRequestQueue(this@signUp)
                queue.add(registerRequest)
            }
        })
    }
}