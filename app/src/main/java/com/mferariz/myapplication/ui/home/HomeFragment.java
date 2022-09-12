package com.mferariz.myapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mferariz.myapplication.controller.UserController;
import com.mferariz.myapplication.ui.adapter.UserAdapter;
import com.mferariz.myapplication.ui.utils.CustomDialog;
import com.mferariz.myapplication.databinding.FragmentHomeBinding;
import com.mferariz.myapplication.model.User;
import com.mferariz.myapplication.model.UsersResponse;
import com.mferariz.myapplication.ui.utils.SnackbarType;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private SearchView searchView;
    private List<User> users;
    private final UserController userController = new UserController();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerViewUsers;
        searchView = binding.searchView;

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(root.getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        userAdapter = new UserAdapter(this.getContext());
        recyclerView.setAdapter(userAdapter);

        searchView.setOnQueryTextListener(this);

        getUsers(root);

        return root;
    }

    private void getUsers(View view) {

        CustomDialog dialog = new CustomDialog(getActivity());
        dialog.startLoadingDialog();
        userController.getUsers().enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if(response.isSuccessful()){
                    users = response.body().getUsers();
                    userAdapter.setUsers(users);
                }
                dialog.dismissDialog();
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                Log.d("getUsers:", t.toString());
                dialog.dismissDialog();
                CustomDialog.showSnackbar(t.toString(), view, SnackbarType.ERROR);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        userAdapter.filter(newText);
        return false;
    }
}