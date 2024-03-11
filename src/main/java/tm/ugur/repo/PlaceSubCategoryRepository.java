package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.PlaceSubCategory;

@Repository
public interface PlaceSubCategoryRepository extends JpaRepository<PlaceSubCategory, Long> {
}
