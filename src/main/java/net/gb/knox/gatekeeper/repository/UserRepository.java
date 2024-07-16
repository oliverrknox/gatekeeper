package net.gb.knox.gatekeeper.repository;

import net.gb.knox.gatekeeper.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
}
