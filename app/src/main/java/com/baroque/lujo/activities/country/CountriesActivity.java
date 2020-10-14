package com.baroque.lujo.activities.country;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.baroque.lujo.R;
import com.baroque.lujo.databinding.ActivityCountriesBinding;

import java.util.List;

import adapters.CountryAdapter;
import utilities.Key;
import utilities.Resource;
import utilities.Utility;

import static utilities.Resource.Status.ERROR;
import static utilities.Resource.Status.SUCCESS;

public class CountriesActivity extends AppCompatActivity implements CountryAdapter.OnItemClickListener, SearchView.OnQueryTextListener {

    private CountryAdapter countryAdapter;
    Context context;
//    LoginViewModel loginViewModel;
    ActivityCountriesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //The ActivityMainBinding class is generated from the xml layout file and it’s named this way because the xml layout file name is turned into
        // Upper CamelCase and appended with Binding, The IDs of the views of the xml layout get converted into camelCase field names
        binding = DataBindingUtil.setContentView(this, R.layout.activity_countries); // instead of setContentView(R.layout.activity_countries);
        //loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class); //use this if we are extending our ViewModel
        binding.setViewModel(new CountryViewModel());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        //Loading list of countries
        binding.getViewModel().getCountries().observe(this, new Observer<Resource<List<CountryModel>>>() {
            @Override
            public void onChanged(Resource<List<CountryModel>> listResource) {
                if (listResource.status == SUCCESS){
                    setCountriesData(listResource.data);
                }else if (listResource.status == ERROR){
//                    Utility.myToastMessage(CountriesActivity.this , listResource.message);
                    Utility.showalert(CountriesActivity.this,"Error",listResource.message,"Dismiss");
                }
            }
        });
        binding.getViewModel().getIsBusy().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.pbProgress.setVisibility(View.VISIBLE);
                }else{
                    binding.pbProgress.setVisibility(View.GONE);
                }
            }
        }) ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setMaxWidth(Integer.MAX_VALUE);  //making it full screen
        return true;
    }

    //// Here is where we are going to implement the filter logic
    @Override
    public boolean onQueryTextChange(String query) {
        countryAdapter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        countryAdapter.filter(query);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setCountriesData(List<CountryModel> countriesData) {
        binding.includeCountriesLayout.rvCountries.setLayoutManager(new LinearLayoutManager(CountriesActivity.this));
        countryAdapter = new CountryAdapter(getApplicationContext(), countriesData);
        countryAdapter.setOnItemClickListener(this);
        binding.includeCountriesLayout.rvCountries.setAdapter(countryAdapter);
    }

    @Override
    public void onItemClick(int position, CountryModel countryModel) {
        Intent intent = new Intent();
        intent.putExtra(Key.KEY_SELECTED_COUNTRY, countryModel);
        setResult(RESULT_OK, intent);
        finish();
    }
}