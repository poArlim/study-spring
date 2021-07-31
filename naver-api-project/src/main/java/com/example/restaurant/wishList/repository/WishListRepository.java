package com.example.restaurant.wishList.repository;

import com.example.restaurant.db.MemoryDbRepositoryAbstract;
import com.example.restaurant.wishList.entity.WishListEntity;
import org.springframework.stereotype.Repository;

@Repository     // 데이터베이스를 저장하는 곳이라는 의미로 붙임.
public class WishListRepository extends MemoryDbRepositoryAbstract<WishListEntity> {

}
