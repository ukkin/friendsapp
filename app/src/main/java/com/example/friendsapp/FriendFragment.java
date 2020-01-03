package com.example.friendsapp;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class FriendFragment extends Fragment {
    private View frndFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups= new ArrayList<>();
    private DatabaseReference FriendRef;
    SearchView searchView;

    /****************************************************/

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    String contactName;
    String contactNumber;
    ArrayList<String> contactnamel = new ArrayList<>();
    ArrayList<String> contactno = new ArrayList<>();


    /****************************************************/

    /****************************************************/

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FriendFragment() {
        // Required empty public constructor
    }
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
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
        frndFragmentView = inflater.inflate(R.layout.fragment_friend, container, false);
        setHasOptionsMenu(true);
        list_view=(ListView) frndFragmentView.findViewById(R.id.list_view);
        //database changes to be done
        FriendRef= FirebaseDatabase.getInstance().getReference().child("user").child("encneonc").child("friends");
        InitializeFields();
        RetrieveContacts();
        RetrieveAndDisplayGroups();
        return frndFragmentView;
    }






    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       // RetrieveContacts();
        SearchManager searchManager =
                (SearchManager)getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        list_view=(ListView) frndFragmentView.findViewById(R.id.list_view);            //check if it is ok to have list_view id here
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                s.toLowerCase(Locale.getDefault());
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
        FriendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final ArrayList<String> set =new ArrayList<>();
                    Map<String,Object> usermap= (HashMap<String,Object>)dataSnapshot.getValue();
                    for(Map.Entry<String,Object> e : usermap.entrySet())
                    {
                      FirebaseDatabase.getInstance().getReference().child("user").child(e.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Object no = dataSnapshot.getValue();
                            set.add(no.toString());
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });
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
    private void RetrieveContacts(){
        Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            int count = 0;
            contactName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));/*
            System.out.println(contactName+" "+contactNumber);*/
            for (String item : contactno) {
                if (item.contains(contactNumber))
                    count++;
            }
            if (count == 0) {
                contactno.add(contactNumber.replaceAll("\\s",""));
                contactnamel.add(contactName);
            }
        }
        phones.close();



        FriendRef= FirebaseDatabase.getInstance().getReference().child("user");
        FriendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> alluser = new ArrayList<>();
                Map<String, Map<String, Object>> usermap = (HashMap<String, Map<String, Object>>) dataSnapshot.getValue();
                /*Collection values = usermap.values();
                Map<String,Object> usermap1 =(Map<String,Object>)(values);*/
                // usermap1= new ArrayList<>(usermap.values());



                for (Map.Entry<String, Map<String, Object>> e : usermap.entrySet()) {

                    FirebaseDatabase.getInstance().getReference().child(e.getKey()).child("phone_no").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String Number = dataSnapshot.getValue().toString();
                            alluser.add(Number);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                   // String value = e.getValue().toString();
                    /*String Number = value.substring(2, 12);
                    alluser.add(Number);*/
                   /* if (contactno.contains(e.getValue().toString())) {
                        FriendRef.child("encneonc").child("friends").child(e.getKey()).setValue("");

                        /// userFound.add(e.getValue().toString());
                    }*/
                }



                for (String item : alluser) {

                    if (contactno.contains(item)||(contactno.contains("+91"+item))) {
                        FriendRef.child("encneonc").child((String) "7043858474").child("friends").child(item).setValue("");
                        System.out.println(item + " RRRRRRRRRRRRRRRRRRRRRRRRRRR ");

                    }
                }
/*
               for(String item : contactno)
                   System.out.println(item + " ");*/


                // System.out.println("Start finding from here");
               /* for(String e : usermap.keySet())
                {
                    for(String f : usermap1.keySet()) {
                        System.out.println("KEY IS " + e + "  VALUE IS  " + f);
                        break;
                    }

                }*/
                /*for(int i=0;i<userFound.size();i++)
                    System.out.println(userFound+" ");*/
                   // System.out.println("No one found or error occured");


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
