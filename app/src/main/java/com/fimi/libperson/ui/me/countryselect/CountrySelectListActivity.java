package com.fimi.libperson.ui.me.countryselect;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import com.fimi.android.app.R;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.libperson.adapter.CountryLetterSortAdapter;
import com.fimi.libperson.widget.TitleView;
import com.fimi.widget.sticklistview.CharacterParser;
import com.fimi.widget.sticklistview.LetterSideBar;
import com.fimi.widget.sticklistview.PinyinComparator;
import com.fimi.widget.sticklistview.SortModel;
import com.fimi.widget.sticklistview.util.StickyListHeadersListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class CountrySelectListActivity extends BaseActivity implements StickyListHeadersListView.OnHeaderClickListener, AdapterView.OnItemClickListener, StickyListHeadersListView.OnLoadingMoreLinstener, CountryLetterSortAdapter.OnShowLetterChangedListener {
    public static final byte LOGIN_REQUEST_CODE = 1;
    public static final byte REGISTER_REQUEST_CODE = 2;
    public static final String SELECT_COUNTRY_AREO_CODE = "select_country_areo_code";
    private static final String TAG = CountrySelectListActivity.class.getSimpleName();
    CountryLetterSortAdapter mAdapter;
    StickyListHeadersListView mStickyLV;
    private CharacterParser mCharacterParser;
    private EditText mEtSearch;
    private ImageButton mIbtnDeleteSearch;
    private LetterSideBar mLetterSideBar;
    private List<SortModel> mSourceDateFilterList = new ArrayList();
    private List<SortModel> mSourceDateList;
    private TitleView mTitleView;

    @Override // com.fimi.kernel.base.BaseActivity
    protected void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override
    // com.fimi.kernel.base.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // com.fimi.kernel.base.BaseActivity
    protected int getContentViewLayoutID() {
        return R.layout.activity_country_select;
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void initData() {
        initView();
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void doTrans() {
        this.mIbtnDeleteSearch.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.countryselect.CountrySelectListActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                CountrySelectListActivity.this.mEtSearch.setText((CharSequence) null);
            }
        });
    }

    public void initView() {
        this.mCharacterParser = CharacterParser.getInstance();
        PinyinComparator pinyinComparator = new PinyinComparator();
        this.mSourceDateList = filledData(getResources().getStringArray(R.array.country_code_list));
        String[] ss = getResources().getStringArray(R.array.common_places_list);
        Collections.sort(this.mSourceDateList, pinyinComparator);
        for (int i = ss.length - 1; i >= 0; i--) {
            int j = 0;
            while (true) {
                if (j < this.mSourceDateList.size()) {
                    if (!this.mSourceDateList.get(j).getName().contains(ss[i])) {
                        j++;
                    } else {
                        this.mSourceDateList.add(0, this.mSourceDateList.remove(j));
                        this.mSourceDateList.get(0).setSortLetter("#");
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        this.mAdapter = new CountryLetterSortAdapter(this, this.mSourceDateList);
        this.mAdapter.setOnShowLetterChangedListener(this);
        this.mStickyLV = (StickyListHeadersListView) findViewById(R.id.stickyList);
        this.mStickyLV.setAdapter((ListAdapter) this.mAdapter);
        this.mStickyLV.setOnItemClickListener(this);
        this.mStickyLV.setOnHeaderClickListener(this);
        this.mStickyLV.setLoadingMoreListener(this);
        this.mLetterSideBar = (LetterSideBar) findViewById(R.id.cs_letter_sb);
        this.mLetterSideBar.setOnTouchingLetterChangedListener(new LetterSideBar.OnTouchingLetterChangedListener() { // from class: com.fimi.libperson.ui.me.countryselect.CountrySelectListActivity.2
            @Override // com.fimi.widget.sticklistview.LetterSideBar.OnTouchingLetterChangedListener
            public void onTouchingLetterChanged(String letter) {
                int jumpPos = CountrySelectListActivity.this.mAdapter.getPositionForSection(letter.charAt(0));
                CountrySelectListActivity.this.mStickyLV.setSelection(jumpPos);
            }
        });
        this.mEtSearch = (EditText) findViewById(R.id.et_cs_search);
        this.mEtSearch.setVisibility(0);
        this.mEtSearch.addTextChangedListener(new TextWatcher() { // from class: com.fimi.libperson.ui.me.countryselect.CountrySelectListActivity.3
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i1, int i22) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i1, int i22) {
                if (charSequence.toString().length() == 0) {
                    CountrySelectListActivity.this.mAdapter.updateListView(CountrySelectListActivity.this.mSourceDateList);
                    CountrySelectListActivity.this.mAdapter.notifyDataSetChanged();
                    return;
                }
                String searchText = CountrySelectListActivity.this.mEtSearch.getText().toString();
                CountrySelectListActivity.this.mSourceDateFilterList.clear();
                for (int h = 0; h < CountrySelectListActivity.this.mSourceDateList.size(); h++) {
                    if (((SortModel) CountrySelectListActivity.this.mSourceDateList.get(h)).getName().contains(searchText)) {
                        CountrySelectListActivity.this.mSourceDateFilterList.add(CountrySelectListActivity.this.mSourceDateList.get(h));
                    }
                }
                if (CountrySelectListActivity.this.mSourceDateFilterList.size() > 0) {
                    CountrySelectListActivity.this.mAdapter.updateListView(CountrySelectListActivity.this.mSourceDateFilterList);
                    CountrySelectListActivity.this.mAdapter.notifyDataSetChanged();
                }
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    if (R.id.et_cs_search == CountrySelectListActivity.this.mEtSearch.getId()) {
                        CountrySelectListActivity.this.mIbtnDeleteSearch.setVisibility(0);
                    }
                } else if (R.id.et_cs_search == CountrySelectListActivity.this.mEtSearch.getId()) {
                    CountrySelectListActivity.this.mIbtnDeleteSearch.setVisibility(8);
                }
            }
        });
        this.mTitleView = (TitleView) findViewById(R.id.title_view);
        this.mTitleView.setTvTitle(this.mContext.getResources().getString(R.string.country_select_title));
        this.mIbtnDeleteSearch = (ImageButton) findViewById(R.id.ibtn_delete_search);
    }

    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<>();
        int n = date.length;
        for (int i = 0; i < n; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            String pinyin = this.mCharacterParser.getSelling(date[i]);
            sortModel.setPinyin(pinyin);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetter(sortString.toUpperCase());
            } else {
                sortModel.setSortLetter("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    @Override // com.fimi.widget.sticklistview.util.StickyListHeadersListView.OnLoadingMoreLinstener
    public void OnLoadingMore() {
    }

    @Override // com.fimi.widget.sticklistview.util.StickyListHeadersListView.OnHeaderClickListener
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String country = ((SortModel) this.mAdapter.getItem(position)).getName();
        setResult(-1, getIntent().putExtra(SELECT_COUNTRY_AREO_CODE, country));
        finish();
    }

    @Override // com.fimi.libperson.adapter.CountryLetterSortAdapter.OnShowLetterChangedListener
    public void onShowLetterChanged(String letter) {
        this.mLetterSideBar.updateLetter(letter);
    }
}
