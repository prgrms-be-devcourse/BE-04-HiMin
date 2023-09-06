package com.prgrms.himin.member.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long> {

	@Query("select a from Address a where a.member.id = :memberId")
	List<Address> findAddressesByMemberId(Long memberId);
}
