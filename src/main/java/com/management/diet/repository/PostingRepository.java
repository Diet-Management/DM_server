package com.management.diet.repository;

import com.management.diet.domain.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {
    public List<Posting> findAllByTitleLike(String keyWord);
    @Query("select p from Posting p order by p.goods desc ")
    List<Posting> findAllOrderByGoodsDesc();
    @Query("select p from Posting p order by p.goods asc ")
    List<Posting> findAllOrderByGoodsAsc();
    @Query("select p from Posting p order by p.date desc ")
    public List<Posting> findAllOrderByDateDesc();
    @Query("select p from Posting p order by p.date asc ")
    public List<Posting> findAllOrderByDateAsc();
}
