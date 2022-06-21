package blog.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Function;

public class GenericSpecificationBuilder<T> {
    public Specification<T> build(Deque<?> postFixedExprStack,
                                  Function<SearchCriteria, Specification<T>> converter) {

        Deque<Specification<T>> specStack = new LinkedList<>();

        while (!postFixedExprStack.isEmpty()) {
            Object mayBeOperand = postFixedExprStack.pollLast();

            if (!(mayBeOperand instanceof String)) {
                specStack.push(converter.apply((SearchCriteria) mayBeOperand));
            } else {
                Specification<T> operand1 = specStack.pop();
                Specification<T> operand2 = specStack.pop();
                if (mayBeOperand.equals(SearchOperation.AND_OPERATOR)) {
                    specStack.push(Specification.where(operand1).and(operand2));
                } else if (mayBeOperand.equals(SearchOperation.OR_OPERATOR)) {
                    specStack.push(Specification.where(operand1).or(operand2));
                }
            }
        }
        return specStack.pop();
    }
}