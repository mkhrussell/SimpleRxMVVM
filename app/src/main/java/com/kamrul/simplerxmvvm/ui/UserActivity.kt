package com.kamrul.simplerxmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.kamrul.simplerxmvvm.Injection
import com.kamrul.simplerxmvvm.databinding.ActivityUserBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var viewModelFactory: UserViewModel.Factory
    private val viewModel: UserViewModel by viewModels { viewModelFactory }

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = Injection.provideViewModelFactory(this)

        binding.updateUserButton.setOnClickListener { updateUserName() }
    }

    override fun onStart() {
        super.onStart()

        disposable.add(viewModel.userName()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { binding.userName.text = it },
                { error -> Log.e(TAG, "Unable to get username", error) }
            ))
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }

    private fun updateUserName() {
        val userName = binding.userNameInput.text.toString()
        // Disable the update button until the user name update has been done
        binding.updateUserButton.isEnabled = false
        // Subscribe to updating the user name.
        // Enable back the button once the user name has been updated
        disposable.add(viewModel.updateUserName(userName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // onComplete
                { binding.updateUserButton.isEnabled = true },
                // onError
                { error -> Log.e(TAG, "Unable to update username", error) })
        )
    }

    companion object {
        private val TAG = UserActivity::class.java.simpleName
    }

}
