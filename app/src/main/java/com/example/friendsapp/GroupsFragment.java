package com.example.friendsapp;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
public class GroupsFragment extends Fragment {
    private View grpFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups= new ArrayList<>();
    private DatabaseReference GroupRef;
    SearchView searchView;
    MenuItem item;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GroupsFragment() {

    }
    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        grpFragmentView = inflater.inflate(R.layout.fragment_groups, container, false);
        setHasOptionsMenu(true);
        list_view=(ListView) grpFragmentView.findViewById(R.id.list_view);
        //database changes to be done
        GroupRef= FirebaseDatabase.getInstance().getReference().child("user").child("nikhar").child("Groups");
        InitializeFields();
        RetrieveAndDisplayGroups();
        return grpFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        SearchManager searchManager =
                (SearchManager)getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        list_view=(ListView) grpFragmentView.findViewById(R.id.list_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s!=null && !s.isEmpty())
                {
                    List<String> lstFound=new ArrayList<String>();
                    for(String item:list_of_groups) {
                        if (item.contains(s))
                            lstFound.add(item);
                    }
                    arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, lstFound);
                    list_view.setAdapter(arrayAdapter);
                }
                else
                {
                    arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_groups);
                    list_view.setAdapter(arrayAdapter);
                }
                return true;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.action_search){
            searchView = (SearchView)item.getActionView();
             InitializeFields();
             RetrieveAndDisplayGroups();
            return true;
        }
        return true;
    }
    private void InitializeFields()
    {

        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_of_groups);
        list_view.setAdapter(arrayAdapter);
    }
    private void RetrieveAndDisplayGroups()
    {
    GroupRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Set<String> set=new HashSet<>();
            Iterator iterator=dataSnapshot.getChildren().iterator();
             while (iterator.hasNext()){
                 set.add(((DataSnapshot)iterator.next()).getKey());
             }
             list_of_groups.clear();
             list_of_groups.addAll(set);
             arrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
