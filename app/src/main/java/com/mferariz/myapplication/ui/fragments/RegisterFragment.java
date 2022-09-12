package com.mferariz.myapplication.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mferariz.myapplication.HomeActivity;
import com.mferariz.myapplication.LoginActivity;
import com.mferariz.myapplication.controller.UserController;
import com.mferariz.myapplication.model.ApiError;
import com.mferariz.myapplication.model.LoginRegisterData;
import com.mferariz.myapplication.model.PageViewModel;
import com.mferariz.myapplication.model.RegisterResult;
import com.mferariz.myapplication.model.Session;
import com.mferariz.myapplication.ui.utils.CustomDialog;
import com.mferariz.myapplication.ui.utils.SnackbarType;
import com.mferariz.myapplication.ui.utils.Validator;
import com.mferariz.myapplication.databinding.FragmentRegisterBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentRegisterBinding binding;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        LoginActivity activity = (LoginActivity) getActivity();
        ViewPager pager = activity.getViewPager();

        binding.goLoginButton.setOnClickListener(view -> {
            pager.setCurrentItem(0, true);
        });

        binding.registerButton.setOnClickListener(view -> {
            registerHandler(view);
        });

        return binding.getRoot();
    }

    private void registerHandler(View view) {
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
        String repPassword = binding.txtRepPassword.getText().toString().trim();
        if(!repPassword.equals(password)){
            CustomDialog.showSnackbar("Las contraseñas deben coincidir", binding.getRoot(), SnackbarType.ALERT);
            return;
        }

        register(email, password, view.getContext());
    }

    private void register(String email, String password, Context context){
        LoginRegisterData registerData = new LoginRegisterData(email, password);
        CustomDialog dialog = new CustomDialog(getActivity());
        dialog.startLoadingDialog();
        userController.register(registerData).enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                if(response.isSuccessful()){
                    Session session = new Session(response.body().getId(), email, password, response.body().getToken());
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
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                dialog.dismissDialog();
                CustomDialog.showSnackbar(t.getMessage(), binding.getRoot(), SnackbarType.ERROR);
            }
        });
    }

}