package hr.lemax.hotel.common.aspect;

import hr.lemax.hotel.model.Hotel;
import hr.lemax.hotel.model.base.SoftDeletedModelBase;
import hr.lemax.hotel.repository.HotelRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@Aspect
@Component
public class SoftDeleteAspect {
    @Autowired
    private ApplicationContext applicationContext;
    @Around("execution(* org.springframework.data.repository.CrudRepository.delete(..)) || " +
            "execution(* org.springframework.data.repository.CrudRepository.deleteAll(..))")
    public Object softDelete(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();
        if(args.length > 0){
            if (args[0] instanceof SoftDeletedModelBase entity) {
                markEntityAsDeleted(entity);
                JpaRepository<Object,Long> repository = findRepositoryForEntity(entity);
                repository.save(entity);  // Save entity after soft delete
                return null;  // Skip hard delete
            } else if (args[0] instanceof Iterable<?> entities) {
                for (Object entity : entities) {
                    if (entity instanceof SoftDeletedModelBase softDeletedEntity) {
                        markEntityAsDeleted(softDeletedEntity);
                    }
                }
                JpaRepository<Object,Long> repository = findRepositoryForEntity(entities.iterator().next());
                repository.saveAll(entities);  // Save all modified entities
                return null;  // Skip hard delete
            }
        }
        return joinPoint.proceed();  // Proceed with normal delete for non-soft-deletable entities
    }

    private void markEntityAsDeleted(SoftDeletedModelBase entity) {
        entity.markAsDeleted(getCurrentUserId());
    }

    @SuppressWarnings("unchecked")
    private <T> JpaRepository<T, Long> findRepositoryForEntity(Object entity) {
        String repositoryBeanName = Character.toLowerCase(entity.getClass().getSimpleName().charAt(0)) +
                entity.getClass().getSimpleName().substring(1) + "Repository";

        // Cast the bean to the correct repository type
        return (JpaRepository<T, Long>) applicationContext.getBean(repositoryBeanName);
    }

    private Long getCurrentUserId() {
        return 1L;  // Placeholder
    }
}
