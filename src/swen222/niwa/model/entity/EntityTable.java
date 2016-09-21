package swen222.niwa.model.entity;

import swen222.niwa.model.world.Location;

import java.util.*;

/**
 * Set implementation that allows for easy lookup of Entities by their Location; formally, Entity.getLocation()
 * Will remain updated with Entities inside it, as long as Entity.setLocation() is not bypassed.
 *
 * @author Marc
 */
public class EntityTable<T extends Entity> extends AbstractSet<T> implements Observer {

	private HashMap<Location, Set<T>> locMap = new HashMap<>();
	private HashSet<T> entries = new HashSet<T>();

	/**
	 * Get all Entities stored in this table at a specified location. Note that the members of this Collection
	 * may change over time. This method makes two guarantees: that at the time of being called, all members of
	 * this Set with the specified Location will be present in the Collection; and that all future members of the
	 * Collection will also have that Location.
	 *
	 * @param loc the Location to find Entities from
	 * @return a Collection view of the entities with Location loc
	 */
	public Set<T> get(Location loc) {
		Set<T> bucket = locMap.get(loc);
		if (bucket == null) return Collections.EMPTY_SET;
		else return Collections.unmodifiableSet(bucket);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		// this method has been coded very defensively, and will throw exceptions at the slightest hint that it is
		// out of sync with its contents

		if (o == null || arg == null) return;
		try {
			T ent = (T) o; // we can't safely cast o to T, so we have to just try, and catch if it fails
			if (!entries.contains(ent)) {
				ent.deleteObserver(this);
				return;
			}

			// LOCATION CHANGES: move ent from its old bucket to the new one
			if (arg instanceof Location) {
				Location oldLoc = (Location) arg;
				Location newLoc = ent.getLocation();
				if (oldLoc.equals(newLoc)) return; // don't need to do anything
				if (!removeFromBuckets(ent, oldLoc, locMap)) { // probably getting trolled - but could mean table bad
					System.err.printf("Lost %s from EntityTable %s under %s", ent, this, oldLoc);
				} else if (!addToBuckets(ent, newLoc, locMap)) { // not as bad
					System.err.printf("%s already in EntityTable %s under %s", ent, this, newLoc);
				}
			} // to track another kind of update, put logic here
		} catch (ClassCastException castFailed) {
			assert false : "EntityTable updated by non-type object: " + o;        // for debugging with -ea
			System.err.println("EntityTable updated by wrong type object: " + o); // we probably want to know about it
			o.deleteObserver(this); // might as well try
		}
	}

	@Override
	public boolean add(T t) {
		if (entries.add(t)) {
			t.addObserver(this);
			return addToBuckets(t, t.getLocation(), locMap);
		} else return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean remove(Object o) {
		if (entries.remove(o)) {
			try {
				T t = (T) o;
				t.deleteObserver(this);
				return removeFromBuckets(t, t.getLocation(), locMap);
			} catch (ClassCastException castFailed) {
				return false;
			}
		} else return false;
	}

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
		for (T t : entries) {
			t.deleteObserver(this);
		}
	}

	@Override
	public Iterator<T> iterator() {
		return entries.iterator();
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
	static <K, V> boolean addToBuckets(V item, K key, Map<K, Set<V>> map) {
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
	static <K, V> boolean removeFromBuckets(V item, K key, Map<K, Set<V>> map) {
		Set<?> bucket = map.get(key);
		if (bucket == null) return false;
		if (!bucket.remove(item)) return false;
		if (bucket.isEmpty()) map.remove(key);
		return true;
	}

}
