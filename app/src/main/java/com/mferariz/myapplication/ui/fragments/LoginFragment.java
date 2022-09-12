package com.mferariz.myapplication.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.mferariz.myapplication.HomeActivity;
import com.mferariz.myapplication.LoginActivity;
import com.mferariz.myapplication.controller.UserController;
import com.mferariz.myapplication.databinding.FragmentLoginBinding;
import com.mferariz.myapplication.model.ApiError;
import com.mferariz.myapplication.model.LoginRegisterData;
import com.mferariz.myapplication.model.LoginResult;
import com.mferariz.myapplication.model.PageViewModel;
import com.mferariz.myapplication.model.Session;
import com.mferariz.myapplication.ui.utils.CustomDialog;
import com.mferariz.myapplication.ui.utils.SnackbarType;
import com.mferariz.myapplication.ui.utils.Validator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentLoginBinding binding;
    private UserController userController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        userController = new UserController(getContext());
        checkSession(this.getContext());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LoginActivity activity = (LoginActivity) getActivity();
        ViewPager pager = activity.getViewPager();

        binding.goRegisterButton.setOnClickListener(view -> {
            pager.setCurrentItem(1, true);
        });
        binding.loginButton.setOnClickListener(view -> {
            loginHandler(view);
        });

        return root;
    }

    private void loginHandler(View view){
        String email = binding.txtEmail.getText().toString().trim();
        if(!Validator.validate(Validator.REGEX_EMAIL, email)){
            CustomDialog.showSnackbar("Ingresa un email valido", binding.getRoot(), SnackbarType.ALERT);
            return;
        }
        String password = binding.txtPassword.getText().toString().trim();
        if(!Validator.validate(Validator.REGEX_PASSWORD, password)){
            CustomDialog.showSnackbar("Ingresa una contraseña de mínimo 6 caracteres", binding.getRoot(), SnackbarType.ALERT);
            return;
        }
        login(email, password, view.getContext());
    }

    private void login(String email, String password, Context context){
        LoginRegisterData loginData = new LoginRegisterData(email, password);
        CustomDialog dialog = new CustomDialog(getActivity());
        dialog.startLoadingDialog();
        userController.login(loginData).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if(response.isSuccessful()){
                    Session session = new Session(0, email, password, response.body().getToken());
                    userController.clearSession();
                    userController.saveSession(session);
                    Intent i = new Intent(context, HomeActivity.class);
                    startActivity(i);
                    getActivity().finish();
                    return;
                }
                ApiError error = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                dialog.dismissDialog();
                CustomDialog.showSnackbar( error.getError(), binding.getRoot(), SnackbarType.ALERT);
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                dialog.dismissDialog();
                CustomDialog.showSnackbar(t.getMessage(), binding.getRoot(), SnackbarType.ERROR);
            }
        });
    }

    private void checkSession(Context context) {
        Session session = userController.getSession();
        if(session == null){
            return;
        }
        login(session.getEmail(), session.getPassword(), context);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}