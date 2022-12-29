package com.example.milwiki.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.milwiki.LoginRequest
import com.example.milwiki.MainActivity
import com.example.milwiki.databinding.FragmentLoginBinding
import com.example.milwiki.signUp
import org.json.JSONException
import org.json.JSONObject


class LoginFragment : Fragment(), View.OnClickListener {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var binding: FragmentLoginBinding

    private lateinit var et_id : EditText
    private lateinit var et_pass : EditText
    private lateinit var btn_login : Button
    private lateinit var btn_register : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        et_id = binding.editId
        et_pass = binding.editPwd
        btn_login = binding.btnLogin
        btn_register = binding.btnSignUp

        binding.btnSignUp.setOnClickListener(this)
        btn_login.setOnClickListener(View.OnClickListener() { //로그인 버튼 수행
            fun onClick(v: View) {
                val userID = et_id.text.toString()
                val userPass = et_pass.text.toString()

            val responseListener: Response.Listener<String> =
                Response.Listener { response ->
                    try {
                        val jasonObject = JSONObject(response)
                        val success = jasonObject.getBoolean("success")
                        if (success) { //회원등록 성공한 경우
                            val userID = jasonObject.getString("userID")
                            val userPass = jasonObject.getString("userPassword")
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(getApplicationContext(), MainActivity::class.java)
                            intent.putExtra("log", "User")
                            intent.putExtra("userID", userID)
                            startActivity(intent)
                        } else { //회원등록 실패한 경우
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT)
                                .show()
                            return@Listener
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            val loginRequest = LoginRequest(userID, userPass, responseListener)
            val queue : RequestQueue = Volley.newRequestQueue(getApplicationContext())
            queue.add(loginRequest)
            }
        })

        return binding.root
    }

    //회원가입 버튼을 누를 시
    override fun onClick(v: View?) {
        val intent = Intent(activity, signUp::class.java)
        startActivity(intent)
    }
}