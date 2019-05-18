package com.philip.nytimessearch.mvvm.model.remote.response_model;

import com.philip.nytimessearch.mvvm.model.local.entity.DocEntity;

import java.util.List;

public class Response {
    private List<DocEntity> docs;
    private Meta meta;

    public List<DocEntity> getDocs() {
        return docs;
    }

    public Meta getMeta() {
        return meta;
    }
}
