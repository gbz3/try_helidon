package com.example.data.repository;

import com.example.data.entity.MyEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;

public class MyRepository {

    private final EntityManagerFactory emf;

    // コンストラクタで EMF を受け取ることで、DI ツールからの注入を容易にする
    public MyRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public MyRepository() {
        // standalone で使用する場合のデフォルトコンストラクタ
        // 通常は Helidon の DI などで EntityManagerFactory を注入することを想定
        // persistence.xml で定義された Persistence Unit 名を指定
        this.emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    }

    public Optional<MyEntity> findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return Optional.ofNullable(em.find(MyEntity.class, id));
        } finally {
            em.close();
        }
    }

    public List<MyEntity> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT e FROM MyEntity e", MyEntity.class).getResultList();
        } finally {
            em.close();
        }
    }

    public MyEntity save(MyEntity entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            if (entity.getId() == null) {
                em.persist(entity);
            } else {
                entity = em.merge(entity);
            }
            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Failed to save entity", e);
        } finally {
            em.close();
        }
    }

    public void delete(MyEntity entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Failed to delete entity", e);
        } finally {
            em.close();
        }
    }

    // 必要に応じて EntityManagerFactory をクローズするメソッド
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
