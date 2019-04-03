package com.example.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {


    private  ProgressBar pbar;
    private  TextView curTemperatureTxt,
            minTemperatureTxt,
            maxTemperatureTxt,
            windspeedText,
            UV_ratingTxt;
    private ImageView weatherImage;

    protected static final String ACTIVITY_NAME = "WeatherForecast";

    protected static final String URL_XML = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=0ec96542f641446f13c2b88bdab89706&mode=xml&units=metric";
    protected static final String URL_IMG = "http://openweathermap.org/img/w/";
    protected static  String URL_UV = "http://api.openweathermap.org/data/2.5/uvi?appid=0ec96542f641446f13c2b88bdab89706";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        pbar = findViewById(R.id.progress_Bar);
        pbar.setVisibility(View.VISIBLE);

        curTemperatureTxt = findViewById(R.id.current_temperature);
        minTemperatureTxt = findViewById(R.id.min_temperature);
        maxTemperatureTxt = findViewById(R.id.max_temperature);
        windspeedText =findViewById(R.id.wind_speed);
        UV_ratingTxt = findViewById(R.id.UV_rating);
        weatherImage = findViewById(R.id.current_weather);

        new ForecastQuery().execute(null, null,null);
    }




    //inner class
    class ForecastQuery extends AsyncTask<String, Integer, String> {
        private String lon,lat, currTemp, minTemp, maxTemp,windspeed, iconFile;
        private  double uv_rating;
        private Bitmap bmap;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(URL_XML);

                HttpURLConnection hcn = (HttpURLConnection)url.openConnection();
                hcn.setRequestMethod("GET");
                hcn.setReadTimeout(10000 /* milliseconds */);
                hcn.setConnectTimeout(15000 /* milliseconds */);

                XmlPullParser parser = Xml.newPullParser();
                //we don't use namespaces
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

                parser.setInput(hcn.getInputStream(), null);
                int eventType = parser.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT){
                    if(eventType == XmlPullParser.START_TAG) {
                        if (parser.getName().equals("temperature")) {
                            currTemp = parser.getAttributeValue(null, "value");
                            Thread.sleep(500);
                            publishProgress(25);
                            minTemp = parser.getAttributeValue(null, "min");
                            Thread.sleep(500);
                            publishProgress(50);
                            maxTemp = parser.getAttributeValue(null, "max");
                            Thread.sleep(500);
                            publishProgress(75);
                        } else if (parser.getName().equals("speed")) {
                            windspeed = parser.getAttributeValue(null, "value");
                        }else if(parser.getName().equals("coord")){
                            lat = parser.getAttributeValue(null, "lat");
                            lon =parser.getAttributeValue(null, "lon");
                        }else if (parser.getName().equals("weather")) {
                            iconFile = parser.getAttributeValue(null, "icon");
                        }
                    }
                    eventType = parser.next();
                }


                hcn.disconnect();



                //retrieve UV_rating by using URL url = new URL(URL_UV);
                String query="&lat="+lat+"&amp;lon="+lon;
                URL_UV +=query;
                URL url_uv = new URL(URL_UV);
                HttpURLConnection hcn_uv = (HttpURLConnection)url_uv.openConnection();
                hcn_uv.setRequestMethod("GET");
                hcn_uv.setRequestProperty("Content-length", "0");
                hcn_uv.setUseCaches(false);
                hcn_uv.setAllowUserInteraction(false);
                hcn_uv.setConnectTimeout(10000);
                hcn_uv.setReadTimeout(15000);
                hcn_uv.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(hcn_uv.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                JSONObject jsonObject = new JSONObject(sb.toString());
                uv_rating = jsonObject.getDouble("value");




                // load image from URL or file
                String image = iconFile + ".png";
                boolean option = isExist(image);
                if(option){
                    Log.i(ACTIVITY_NAME, "Weather image exists, loading file");
                    FileInputStream fis = openFileInput(image);
                    bmap = BitmapFactory.decodeStream(fis);

                }else {
                    Log.i(ACTIVITY_NAME,"Weather image does exist, trying to load URL");
                    hcn = (HttpURLConnection) new URL(URL_IMG + image).openConnection();
                    hcn.connect();
                    int responseCode = hcn.getResponseCode();
                    if (responseCode == 200) {
                        bmap = BitmapFactory.decodeStream(hcn.getInputStream());
                    }
                    FileOutputStream fos = openFileOutput(image, Context.MODE_PRIVATE);
                    bmap.compress(Bitmap.CompressFormat.PNG,80,fos);
                    fos.flush();
                    fos.close();
                    hcn.disconnect();
                    Thread.sleep(500);
                    publishProgress(100);
                }

            }catch (MalformedURLException e){
                e.printStackTrace();

            }catch (XmlPullParserException xe){
                xe.printStackTrace();
            }catch(FileNotFoundException fe){
                fe.printStackTrace();
            }
            catch (IOException ie){
                ie.printStackTrace();
            } catch (JSONException je) {
                je.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected  void onProgressUpdate(Integer ... value){
            pbar.setVisibility(View.VISIBLE);
            pbar.setProgress(value[0]);
        }

        @Override
        protected  void onPostExecute(String args){
            pbar.setVisibility(View.INVISIBLE);
            curTemperatureTxt.setText(getString(R.string.currTemp) + currTemp + "℃");
            minTemperatureTxt.setText(getString(R.string.minTemp) + minTemp + "℃");
            maxTemperatureTxt.setText(getString(R.string.maxTemp) + maxTemp + "℃");
            windspeedText.setText(getString(R.string.wind_speed) + windspeed + "m/s");
            UV_ratingTxt.setText(getString(R.string.UV_rating) + uv_rating + "/rate");

            weatherImage.setImageBitmap(bmap);
        }


        private boolean isExist(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();

        }
    }
}