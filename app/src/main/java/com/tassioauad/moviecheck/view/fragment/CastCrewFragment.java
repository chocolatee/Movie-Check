package com.tassioauad.moviecheck.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tassioauad.moviecheck.MovieCheckApplication;
import com.tassioauad.moviecheck.R;
import com.tassioauad.moviecheck.dagger.CastCrewViewModule;
import com.tassioauad.moviecheck.model.entity.Cast;
import com.tassioauad.moviecheck.model.entity.Crew;
import com.tassioauad.moviecheck.model.entity.Movie;
import com.tassioauad.moviecheck.presenter.CastCrewPresenter;
import com.tassioauad.moviecheck.view.CastCrewView;
import com.tassioauad.moviecheck.view.adapter.CastListAdapter;
import com.tassioauad.moviecheck.view.adapter.CrewListAdapter;
import com.tassioauad.moviecheck.view.adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CastCrewFragment extends Fragment implements CastCrewView {

    @Inject
    CastCrewPresenter presenter;

    private List<Cast> castList;
    private List<Crew> crewList;
    private static final String KEY_CREWLIST = "CREWLIST";
    private static final String KEY_CASTLIST = "CASTLIST";
    private static final String KEY_MOVIE = "MOVIE";

    @Bind(R.id.recyclerview_cast)
    RecyclerView recyclerViewCast;
    @Bind(R.id.recyclerview_crew)
    RecyclerView recyclerViewCrew;
    @Bind(R.id.linearlayout_cast_anyfounded)
    LinearLayout linearLayoutAnyCastFounded;
    @Bind(R.id.linearlayout_cast_loadfailed)
    LinearLayout linearLayoutCastLoadFailed;
    @Bind(R.id.progressbar_cast)
    ProgressBar progressBarCast;
    @Bind(R.id.linearlayout_crew_anyfounded)
    LinearLayout linearLayoutAnyCrewFounded;
    @Bind(R.id.linearlayout_crew_loadfailed)
    LinearLayout linearLayoutCrewLoadFailed;
    @Bind(R.id.progressbar_crew)
    ProgressBar progressBarCrew;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MovieCheckApplication) getActivity().getApplication())
                .getObjectGraph().plus(new CastCrewViewModule(this)).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_castcrew, container, false);
        ButterKnife.bind(this, view);

        if(savedInstanceState != null) {
            crewList = savedInstanceState.getParcelableArrayList(KEY_CREWLIST);
            castList = savedInstanceState.getParcelableArrayList(KEY_CASTLIST);
            if(castList != null) {
                showCasts(castList);
            }
            if(crewList != null) {
                showCrews(crewList);
            }
        }

        return view;
    }

    @Override
    public void onResume() {
        if(castList == null) {
            presenter.loadCast((Movie) getArguments().getParcelable(KEY_MOVIE));
        }
        if(crewList == null) {
            presenter.loadCrew((Movie) getArguments().getParcelable(KEY_MOVIE));
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        presenter.stop();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(crewList != null) {
            outState.putParcelableArrayList(KEY_CREWLIST, new ArrayList<>(crewList));
        }
        if(castList != null) {
            outState.putParcelableArrayList(KEY_CASTLIST, new ArrayList<>(castList));
        }
    }

    public static CastCrewFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_MOVIE, movie);
        CastCrewFragment fragment = new CastCrewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void hideLoadingCrew() {
        progressBarCrew.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingCrew() {
        progressBarCrew.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCrews(List<Crew> crewList) {
        this.crewList = crewList;
        linearLayoutCrewLoadFailed.setVisibility(View.GONE);
        linearLayoutAnyCrewFounded.setVisibility(View.GONE);
        recyclerViewCrew.setVisibility(View.VISIBLE);
        recyclerViewCrew.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerViewCrew.setAdapter(new CrewListAdapter(crewList, new OnItemClickListener<Crew>() {
            @Override
            public void onClick(Crew crew) {

            }
        }));
    }

    @Override
    public void warnAnyCrewFounded() {
        linearLayoutCrewLoadFailed.setVisibility(View.GONE);
        linearLayoutAnyCrewFounded.setVisibility(View.VISIBLE);
        recyclerViewCrew.setVisibility(View.GONE);
    }

    @Override
    public void warnFailedToLoadCrews() {
        linearLayoutCrewLoadFailed.setVisibility(View.VISIBLE);
        linearLayoutAnyCrewFounded.setVisibility(View.GONE);
        recyclerViewCrew.setVisibility(View.GONE);
    }

    @Override
    public void hideLoadingCast() {
        progressBarCast.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingCast() {
        progressBarCast.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCasts(List<Cast> castList) {
        this.castList = castList;
        linearLayoutCastLoadFailed.setVisibility(View.GONE);
        linearLayoutAnyCastFounded.setVisibility(View.GONE);
        recyclerViewCast.setVisibility(View.VISIBLE);
        recyclerViewCast.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerViewCast.setAdapter(new CastListAdapter(castList, new OnItemClickListener<Cast>() {
            @Override
            public void onClick(Cast cast) {

            }
        }));
    }

    @Override
    public void warnAnyCastFounded() {
        linearLayoutCastLoadFailed.setVisibility(View.GONE);
        linearLayoutAnyCastFounded.setVisibility(View.VISIBLE);
        recyclerViewCast.setVisibility(View.GONE);
    }

    @Override
    public void warnFailedToLoadCasts() {
        linearLayoutCastLoadFailed.setVisibility(View.VISIBLE);
        linearLayoutAnyCastFounded.setVisibility(View.GONE);
        recyclerViewCast.setVisibility(View.GONE);
    }
}