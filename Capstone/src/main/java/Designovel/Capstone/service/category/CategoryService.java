package Designovel.Capstone.service.category;

import Designovel.Capstone.domain.CategoryNode;
import Designovel.Capstone.entity.Category;
import Designovel.Capstone.entity.CategoryClosure;
import Designovel.Capstone.repository.category.CategoryClosureRepository;
import Designovel.Capstone.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryClosureRepository categoryClosureRepository;

    private static List<CategoryNode> findRootNodes(List<CategoryClosure> closures, Map<Integer, CategoryNode> nodeMap) {
        Set<Integer> descendantNodeIds = new HashSet<>();
        List<CategoryNode> roots = new ArrayList<>();

        for (CategoryClosure closure : closures) {
            //depth가 1인것의 descendant들은 무조건 Root노드가 아님
            if (closure.getDepth() == 1) {
                descendantNodeIds.add(closure.getDescendantId().getCategoryId());
            }
        }
        for (CategoryNode node : nodeMap.values()) {
            // node의 ID가 자손 ID 집합에 포함되지 않으면 최상위 노드
            if (!descendantNodeIds.contains(node.getCategoryId())) {
                roots.add(node);
            }
        }
        return roots;
    }

    public void addChildNodeToParentNode(List<CategoryClosure> closures, Map<Integer, CategoryNode> nodeMap) {

        for (CategoryClosure closure : closures) {
            CategoryNode parent = nodeMap.get(closure.getAncestorId().getCategoryId());
            CategoryNode child = nodeMap.get(closure.getDescendantId().getCategoryId());

            if (!parent.equals(child) && closure.getDepth() == 1) {
                parent.getChildren().add(child);
            }
        }
    }

    public List<CategoryNode> getCategoryTree(String mallTypeId) {
        List<Category> categories = categoryRepository.findByMallType_MallTypeId(mallTypeId);
        List<CategoryClosure> closures = categoryClosureRepository.findByMallType_MallTypeId(mallTypeId);
        return buildCategoryTree(categories, closures);
    }

    private List<CategoryNode> buildCategoryTree(List<Category> categories, List<CategoryClosure> closures) {
        Map<Integer, CategoryNode> nodeMap = new HashMap<>();

        // 모든 카테고리를 CategoryNode로 변환하여 맵에 저장
        for (Category category : categories) {
            nodeMap.put(category.getCategoryId(), new CategoryNode(category.getCategoryId(), category.getName(), new ArrayList<>()));
        }

        // closures를 사용하여 계층 구조 빌드
        addChildNodeToParentNode(closures, nodeMap);
        return findRootNodes(closures, nodeMap);
    }
}
