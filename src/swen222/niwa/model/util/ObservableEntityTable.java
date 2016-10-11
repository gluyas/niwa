package swen222.niwa.model.util;

import swen222.niwa.model.entity.Entity;

import java.util.Observable;
import java.util.Observer;

/**
 * EntityTable which utilises the Observer pattern. Subclasses must ensure that all Entities added to this Collection
 * have this added as an Observer, and removed on remove. This class notifies its Observers with any updates dispatched
 * by Entities contained within it.
 *
 * @author Marc
 */
public abstract class ObservableEntityTable<E extends Entity>
			extends Observable implements EntityTable<E>, Observer {

	@Override
	public final boolean add(E e) {
		if (addImpl(e)) {
			e.addObserver(this);
			setChanged();
			notifyObservers(new AddElementUpdate(e));
			return true;
		} else return false;
	}

	/**
	 * Concrete implementation of the add method, which is final in ObservableEntityTable.
	 * This method is called by add(E); if it returns true, e will be Observed by this.
	 * This should satisfy the specifications of Collection.add.
	 * @param e Entity to add
	 * @return true if the underlying Collection was modified; false if not
	 */
	protected abstract boolean addImpl(E e);

	@Override
	@SuppressWarnings("unchecked")
	public final boolean remove(Object o) {
		if (removeImpl(o)) {
			E e = (E) o;
			e.deleteObserver(this);
			setChanged();
			notifyObservers(new RemoveElementUpdate(e));
			return true;
		} else return false;
	}

	/**
	 * Concrete implementation of the remove method, which is final in ObservableEntityTable.
	 * This method is called by remove(Object); if it returns true, o will stop being observed by this
	 * This should satisfy the specifications of Collection.remove.
	 * @param o Object to remove
	 * @return true if the underlying Collection was modified; false if not
	 */
	protected abstract boolean removeImpl(Object o);

	@Override
	public final void update(Observable o, Object arg) {
		//System.out.printf("%s -> %s -> %s", o, this, arg);
		updateImpl(o, arg);
		setChanged();
		notifyObservers(arg);
	}

	/**
	 * Optional implementation for subclasses which need to react to updates from their contents.
	 * This method is called every time update is, with the same parameters. Note that the implementation of
	 * update already notifies the observers of this class.
	 * @param o the Observable object who notified this
	 * @param arg the argument provided in their notifyObservers call
	 */
	protected void updateImpl(Observable o, Object arg) {
		// implementation specific - optional
	}

	public abstract class ElementUpdate implements Update {

		public final E e;

		public ElementUpdate(E e) {
			this.e = e;
		}
	}

	public class AddElementUpdate extends ElementUpdate {

		public AddElementUpdate(E e) {
			super(e);
		}

		@Override
		public void apply() {
			ObservableEntityTable.this.add(e);
		}
	}

	public class RemoveElementUpdate extends ElementUpdate {

		public RemoveElementUpdate(E e) {
			super(e);
		}

		@Override
		public void apply() {
			ObservableEntityTable.this.remove(e);
		}
	}

}
