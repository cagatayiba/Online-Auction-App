package com.cengo.muzayedebackendv2.repository;

import com.cengo.muzayedebackendv2.domain.view.LotAuctionDetail;
import com.cengo.muzayedebackendv2.domain.view.key.LotAuctionDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LotAuctionDetailRepository extends JpaRepository<LotAuctionDetail, LotAuctionDetailKey>, JpaSpecificationExecutor<LotAuctionDetail> {



}
