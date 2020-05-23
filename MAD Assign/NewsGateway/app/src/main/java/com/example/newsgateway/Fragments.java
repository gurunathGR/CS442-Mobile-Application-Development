package com.example.newsgateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fragments extends Fragment
{
    private static final String TAG = "Fragments of Article";

    TextView title;
    TextView date;
    TextView author;
    TextView content;
    ImageView photo;
    TextView count;
    Articles articleObj;
    int counter;
    View v;

    public static final String ARTICLE = "ARTICLE";
    public static final String INDEX = "INDEX";
    public static final String TOTAL = "TOTAL";

    public static final Fragments newFragment(Articles article, int index, int total)
    {
        Fragments frag = new Fragments();
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(ARTICLE, article);
        bundle.putInt(INDEX, index);
        bundle.putInt(TOTAL, total);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        articleObj = (Articles) getArguments().getSerializable(ARTICLE);
        counter = getArguments().getInt(INDEX)+1;
        int total = getArguments().getInt(TOTAL);
        String endLine = counter +" of "+total;


        v = inflater.inflate(R.layout.fragment, container, false);
        title = (TextView)v.findViewById(R.id.HEADLINE);
        date = (TextView) v.findViewById(R.id.DATE);
        author = (TextView) v.findViewById(R.id.AUTHOR);
        content = (TextView) v.findViewById(R.id.CONTENT);
        count = (TextView) v.findViewById(R.id.INDEX);
        photo = (ImageView) v.findViewById(R.id.PHOTO);

        count.setText(endLine);
        if(articleObj.getaTitle() != null){ title.setText(articleObj.getaTitle());
        }
        else{
            title.setText("");}

        if(articleObj.getaPublishedAt() !=null && !articleObj.getaPublishedAt().isEmpty()) {

            String sDate1 = articleObj.getaPublishedAt();

            Date date1 = null;
            String publisheddate = "";
            try {
                if(sDate1 != null){

                    date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sDate1);}
                String pattern = "MMM dd, yyyy HH:mm";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                publisheddate = simpleDateFormat.format(date1);
                date.setText(publisheddate);
            } catch (ParseException e) {

            }
        }
        if(articleObj.getaDescription()!=null) {author.setText(articleObj.getaDescription());}
        else{author.setText("");}

        if(articleObj.getaAuthor() != null) {content.setText(articleObj.getaAuthor());}
        else{content.setText("");}

        author.setMovementMethod(new ScrollingMovementMethod());

        if(articleObj.getaUrlToImage()!=null){
            imageLoad(articleObj.getaUrlToImage());}

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(articleObj.getArticleUrl()));
                startActivity(intent);
            }
        });

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(articleObj.getArticleUrl()));
                startActivity(intent);
            }
        });

        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(articleObj.getArticleUrl()));
                startActivity(intent);
            }
        });


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(articleObj.getArticleUrl()));
                startActivity(intent);
            }
        });

        return v;
    }


    private void imageLoad(final String imageURL)
    {

        if (imageURL != null) {
            Picasso picasso = new Picasso.Builder(getActivity()).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {

                    final String changedUrl = imageURL.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .fit()
                            .centerCrop()
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(photo);
                }
            }).build();
            picasso.load(imageURL)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photo);
        } else {
            Picasso.with(getActivity()).load(imageURL)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(photo);
        }
    }
}

