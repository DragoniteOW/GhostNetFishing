import java.util.List;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Named
@ApplicationScoped
public class GhostNetDAO {

    EntityManager entityManager;

    CriteriaBuilder criteriaBuilder;

    public GhostNetDAO() {
        try {
            entityManager = Persistence.createEntityManagerFactory("GhostNetFishing").createEntityManager();
            criteriaBuilder = entityManager.getCriteriaBuilder();

            // init falls noch leer
            long count = getGhostNetCount();
            System.err.println("Aktuell " + count+ " Ghost Nets.");

            if(count == 0) {
                System.err.println("Initialisierung der Daten.");
                EntityTransaction t = getAndBeginTransaction();
                for(GhostNet net : SheaSepherd.initGhostNets) 
                {
                    persist(net);
                }
                t.commit();	
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public long getGhostNetCount() {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(GhostNet.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    public GhostNet getGhostNetAtIndex(int pos) {
        CriteriaQuery<GhostNet> query = criteriaBuilder.createQuery(GhostNet.class);
        Root<GhostNet> variableRoot = query.from(GhostNet.class);
        return entityManager.createQuery(query).setMaxResults(1).setFirstResult(pos).getSingleResult();
    }
    
    public GhostNet getGhostNetWithKey(int key) {
        CriteriaQuery<GhostNet> query = criteriaBuilder.createQuery(GhostNet.class);
        Root<GhostNet> variableRoot = query.from(GhostNet.class);
        GhostNet n = (GhostNet) entityManager.createQuery(query).setMaxResults(1).getSingleResult();
        return n;
    }
    
    public List<GhostNet> getGhostNets() {
        CriteriaQuery<GhostNet> query = criteriaBuilder.createQuery(GhostNet.class);
        Root<GhostNet> variableRoot = query.from(GhostNet.class);
        return entityManager.createQuery(query).getResultList();
    }

    public EntityTransaction getAndBeginTransaction() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        return transaction;
    }

    public void merge(GhostNet net) {
        entityManager.merge(net);
    }
    
    public void persist(GhostNet net) {
        entityManager.persist(net);
    }

    public static void main(String[] args) {
        GhostNetDAO dao = new GhostNetDAO();
        System.err.println("Wir haben " + dao.getGhostNetCount() + " GhostNets.");
    }



}
