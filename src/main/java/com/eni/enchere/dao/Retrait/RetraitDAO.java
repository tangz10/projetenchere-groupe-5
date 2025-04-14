package com.eni.enchere.dao.Retrait;

import com.eni.enchere.bo.Retrait;

public interface RetraitDAO {
    void insert(Retrait retrait);
    Retrait selectByArticle(long noArticle);
}
