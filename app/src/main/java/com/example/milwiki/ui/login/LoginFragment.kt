package com.example.milwiki.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.milwiki.databinding.FragmentLoginBinding
import com.example.milwiki.signUp

class LoginFragment : Fragment(), View.OnClickListener {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btnSignUp.setOnClickListener(this)

        return binding.root
    }

    //회원가입 창으로 이동
    override fun onClick(v: View?) {
        val intent = Intent(activity, signUp::class.java)
        startActivity(intent)
    }
}