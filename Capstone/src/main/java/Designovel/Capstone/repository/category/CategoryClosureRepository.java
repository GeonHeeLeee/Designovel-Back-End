package Designovel.Capstone.repository.category;

import Designovel.Capstone.entity.CategoryClosure;
import Designovel.Capstone.entity.id.CategoryClosureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryClosureRepository extends JpaRepository<CategoryClosure, CategoryClosureId> {

    List<CategoryClosure> findByMallType_MallTypeId(String mallType);
}
