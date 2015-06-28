package com.nanoudacity.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {

    private ArrayAdapter<String> mSearchResultAdapter;

    public SearchFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateSearchResult();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSearchResultAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_search_result,
                        R.id.list_item_search_result_view,
                        new ArrayList<String>());
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView_searchResult);
        listView.setAdapter(mSearchResultAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return rootView;
    }

    private void updateSearchResult() {
        FetchResultTask searchResultTask = new FetchResultTask();
        searchResultTask.execute("coldplay");
    }

    public class FetchResultTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchResultTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            if(params.length == 0)
                return null;
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);
            return getArtistsFromResult(results);
        }

        private String[] getArtistsFromResult(ArtistsPager results) {
            List<Artist> artists = results.artists.items;
            String[] artistResult = new String[artists.size()];
            for(int i = 0; i < artists.size(); i++)
                artistResult[i] = artists.get(i).name;
            return artistResult;
        }

        @Override
        protected void onPostExecute(String[] results) {
            if(results != null) {
                mSearchResultAdapter.clear();
                for(String result: results)
                    mSearchResultAdapter.add(result);
            }

        }
    }



}
