package ex.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Interface anInterface;
    private List<Location.LocationData> locationDataList;
    private LatLng latlng;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment =new SupportMapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);
    }

    private void apiCall() {
        anInterface = Client.getClient().create(Interface.class);
        final Observable<Response<Location>> locationdataCall = anInterface.fetchLocationData(Constants.MS_DISCOVER_FUNCTION, Constants.API_KEY, Constants.USER_ID);
        locationdataCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Location>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response<Location> value) {
                        if (value.isSuccessful()) {

                            locationDataList = value.body().getLocationData();

                            for (int i = 0; i < 20; i++) {
                                latlng = new LatLng(Double.valueOf(locationDataList.get(i).getLatitude()), Double.valueOf(locationDataList.get(i).getLongitude()));
                                googleMap.addMarker(new MarkerOptions().position(latlng)
                                        .title(locationDataList.get(i).getName()));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16f));
                            }


                        }
                    }

                    @Override
                    public void onError(Throwable e) {


                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        apiCall();
    }

}
