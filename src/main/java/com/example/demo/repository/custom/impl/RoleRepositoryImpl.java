package com.example.demo.repository.custom.impl;

import com.example.demo.entity.RoleEntity;
import com.example.demo.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;


@Repository
public class RoleRepositoryImpl implements RoleRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public RoleEntity findOneByCode(String code) {
		String sql = "select * FROM role as r where r.code = '" + code + "'";
		Query query = entityManager.createNativeQuery(sql, RoleEntity.class);
		return (RoleEntity) query.getSingleResult();
	}

	@Override
	public <S extends RoleEntity> S save(S entity) {
		return null;
	}

	@Override
	public <S extends RoleEntity> List<S> saveAll(Iterable<S> entities) {
		return List.of();
	}

	@Override
	public Optional<RoleEntity> findById(Long aLong) {
		return Optional.empty();
	}

	@Override
	public boolean existsById(Long aLong) {
		return false;
	}

	@Override
	public List<RoleEntity> findAll() {
		String sql = "select * FROM role as r";
		Query query = entityManager.createNativeQuery(sql, RoleEntity.class);
		return query.getResultList();
	}

	@Override
	public List<RoleEntity> findAllById(Iterable<Long> longs) {
		return List.of();
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void deleteById(Long aLong) {

	}

	@Override
	public void delete(RoleEntity entity) {

	}

	@Override
	public void deleteAllById(Iterable<? extends Long> longs) {

	}

	@Override
	public void deleteAll(Iterable<? extends RoleEntity> entities) {

	}

	@Override
	public void deleteAll() {

	}

	@Override
	public void flush() {

	}

	@Override
	public <S extends RoleEntity> S saveAndFlush(S entity) {
		return null;
	}

	@Override
	public <S extends RoleEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
		return List.of();
	}

	@Override
	public void deleteAllInBatch(Iterable<RoleEntity> entities) {

	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> longs) {

	}

	@Override
	public void deleteAllInBatch() {

	}

	@Override
	public RoleEntity getOne(Long aLong) {
		return null;
	}

	@Override
	public RoleEntity getById(Long aLong) {
		return null;
	}

	@Override
	public RoleEntity getReferenceById(Long aLong) {
		return null;
	}

	@Override
	public <S extends RoleEntity> Optional<S> findOne(Example<S> example) {
		return Optional.empty();
	}

	@Override
	public <S extends RoleEntity> List<S> findAll(Example<S> example) {
		return List.of();
	}

	@Override
	public <S extends RoleEntity> List<S> findAll(Example<S> example, Sort sort) {
		return List.of();
	}

	@Override
	public <S extends RoleEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
		return null;
	}

	@Override
	public <S extends RoleEntity> long count(Example<S> example) {
		return 0;
	}

	@Override
	public <S extends RoleEntity> boolean exists(Example<S> example) {
		return false;
	}

	@Override
	public <S extends RoleEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
		return null;
	}

	@Override
	public List<RoleEntity> findAll(Sort sort) {
		return List.of();
	}

	@Override
	public Page<RoleEntity> findAll(Pageable pageable) {
		return null;
	}
}
