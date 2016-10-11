package swen222.niwa.model.util;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.world.Location;

import java.util.*;

/**
 * Set implementation that allows for easy lookup of Entities by their Location; formally, Entity.getLocation()
 * Will remain updated with Entities inside it, as long as Entity.moveTo() is not bypassed. Uses Hashed Collections
 * internally.
 *
 * @author Marc
 */
public class HashEntityTable<E extends Entity> extends ObservableEntityTable<E> {

	private HashMap<Location, Set<E>> locMap = new HashMap<>();
	private HashSet<E> entries = new HashSet<>();

	/**
	 * Get all Entities stored in this table at a specified location. Note that the members of this Collection
	 * may change over time. This method makes two guarantees: that at the time of being called, all members of
	 * this Set with the specified Location will be present in the Collection; and that all future members of the
	 * Collection will also have that Location.
	 *
	 * @param loc the Location to find Entities from
	 * @return a Collection view of the entities with Location loc
	 */
	public Set<E> get(Location loc) {
		Set<E> bucket = locMap.get(loc);
		//System.out.print(bucket);
		if (bucket == null) return Collections.EMPTY_SET;
		else return Collections.unmodifiableSet(bucket);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void updateImpl(Observable o, Object arg) {
		// this method has been coded very defensively, and will throw exceptions at the slightest hint that it is
		// out of sync with its contents

		if (o == null || arg == null) return;
		try {
			E ent = (E) o; // we can't safely cast o to E, so we have to just try, and catch if it fails
			if (!entries.contains(ent)) {
				ent.deleteObserver(this);
				return;
			}

			// LOCATION CHANGES: move ent from its old bucket to the new one
			if (arg instanceof Entity.LocationUpdate) {
				Entity.LocationUpdate locUD = (Entity.LocationUpdate) arg;
				if (locUD.to.equals(locUD.from)) return; // don't need to do anything
				if (!removeFromBuckets(ent, locUD.from, locMap)) { // probably getting trolled - but could be table bad
					System.err.printf("Lost %s from HashEntityTable %s under %s", ent, this, locUD.from);
				} else if (!addToBuckets(ent, locUD.to, locMap)) { // not as bad
					System.err.printf("%s already in HashEntityTable %s under %s", ent, this, locUD.to);
				}
			} // to track another kind of update, put logic here
		} catch (ClassCastException castFailed) {
			assert false : "HashEntityTable updated by non-type object: " + o;        // for debugging with -ea
			System.err.println("HashEntityTable updated by wrong type object: " + o); // we probably want to know
			o.deleteObserver(this); // might as well try
		}
	}

	@Override
	public boolean addImpl(E e) {
		if (entries.add(e)) {
			if (!addToBuckets(e, e.getLocation(), locMap)) {
				System.err.println(String.format("%s already existed in buckets of %s", e, this));
			}
			return true;
		} else return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean removeImpl(Object o) {
		if (entries.remove(o)) {
			E e = (E) o; // remove won't return true unless o instanceof E
			if (!removeFromBuckets(e, e.getLocation(), locMap)) {
				System.err.println(String.format("%s could not be removed from buckets of %s", e, this));
			}
			return true;
		} else return false;
	}

	// generic helper methods - can easily be used if we wish to track another property

	/**
	 * Add an item to a map of buckets, under the specified key
	 * @param item item to add
	 * @param key key to get the item's bucket
	 * @param map Map containing the buckets
	 * @param <K> key type of map
	 * @param <V> value type of the buckets
	 * @return false if the map already contained the item
	 */
	protected static <K, V> boolean addToBuckets(V item, K key, Map<K, Set<V>> map) {
		Set<V> bucket = map.get(key);
		if (bucket == null) {
			bucket = new HashSet<>();
			map.put(key, bucket);
		}
		return bucket.add(item);
	}

	/**
	 * Remove an item from a map of buckets, under the specified key
	 * @param item item to remove
	 * @param key key to get the item's bucket
	 * @param map Map containing the buckets
	 * @param <K> key type of map
	 * @param <V> value type of the buckets
	 * @return false if the bucket did not exist, or item was not in it
	 */
	protected static <K, V> boolean removeFromBuckets(V item, K key, Map<K, Set<V>> map) {
		Set<?> bucket = map.get(key);
		if (bucket == null) return false;
		if (!bucket.remove(item)) return false;
		if (bucket.isEmpty()) map.remove(key);
		return true;
	}

	// trivial Set implementation

	@Override
	public int size() {
		return entries.size();
	}

	@Override
	public boolean contains(Object o) {
		return entries.contains(o);
	}

	@Override
	public void clear() {
		for (E e : entries) {
			e.deleteObserver(this);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return entries.iterator();
	}

	@Override
	public boolean isEmpty() {
		return entries.isEmpty();
	}

	@Override
	public Object[] toArray() {
		return entries.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return entries.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return entries.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean changed = false;
		for (E e : c) {
			if (add(e)) changed = true;
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed = false;
		for (Object o : c) {
			if (!contains(o) && remove(o)) changed = true;
		}
		return changed;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object o : c) {
			if (remove(o)) changed = true;
		}
		return changed;
	}


}
