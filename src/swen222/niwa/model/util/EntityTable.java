package swen222.niwa.model.util;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.world.Location;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Set subinterface that provides easy access to stored Entities by their Location.
 *
 * @author Marc
 */
public interface EntityTable<T extends Entity> extends Set<T>, Serializable {

	/**
	 * Get all Entities stored in this table at a specified location. Note that the members of this Collection
	 * may change over time. This method makes two guarantees: that at the time of being called, all members of
	 * this Set with the specified Location will be present in the Collection; and that all future members of the
	 * Collection will also have that Location.
	 *
	 * @param loc the Location to find Entities from
	 * @return a Collection view of the entities with Location loc
	 */
	Set<T> get(Location loc);

	static <T extends Entity> EntityTable<T> unmodifiable(EntityTable<T> et) {
		return new UnmodifiableEntityTable<>(et);
	}

	/**
	 * Basic wrapper class to prevent external modification
	 */
	class UnmodifiableEntityTable<T extends Entity> implements EntityTable<T> {

		private final EntityTable parent;

		public UnmodifiableEntityTable(EntityTable<T> parent) {
			this.parent = parent;
		}

		@Override
		public boolean add(T t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator iterator() {
			return parent.iterator();
		}

		@Override
		public int size() {
			return parent.size();
		}

		@Override
		public Set get(Location loc) {
			return parent.get(loc);
		}

		@Override
		public boolean isEmpty() {
			return parent.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			return parent.contains(o);
		}

		@Override
		public Object[] toArray() {
			return parent.toArray();
		}

		@Override
		public Object[] toArray(Object[] a) {
			return parent.toArray(a);
		}

		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsAll(Collection c) {
			return parent.containsAll(c);
		}

		@Override
		public boolean addAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean retainAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}
	}
}
