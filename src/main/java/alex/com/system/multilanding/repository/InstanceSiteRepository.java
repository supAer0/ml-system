package alex.com.system.multilanding.repository;

import alex.com.system.multilanding.model.InstanceSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstanceSiteRepository extends JpaRepository<InstanceSite, Long> {
}