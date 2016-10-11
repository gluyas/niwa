package swen222.niwa;

import com.sun.istack.internal.Nullable;
import swen222.niwa.file.SpriteLoader;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.entity.PlayerEntity;
import swen222.niwa.model.util.HashEntityTable;
import swen222.niwa.model.util.ObservableEntityTable;
import swen222.niwa.model.world.*;
import swen222.niwa.net.Master;

import java.util.*;

/**
 *
 * @author Marc
 */
public class Server {

	public static final int MAX_PLAYERS = 5;

	public final World world; // immutable object
	private final ObservableEntityTable<Entity>[][] tables;
	private final Rules rules;
	private final Map<Master, PlayerEntity> connections = new HashMap<>(5);

	// Create a server
	@SuppressWarnings("unchecked")
	public Server(World world, ObservableEntityTable<Entity>[][] tables) {
		this.world = world;
		this.tables = tables;
//		this.rules = new Rules[world.height][world.width];
//		for (int row = 0; row < world.height; row++) {
//			for (int col = 0; col < world.width; col++) {
//				rules[row][col] = new Rules(world, tables[row][col],tables);
//			}
//		}
		rules = new Rules(world, tables);
	}

	// MASTER ACTIONS

	/**
	 * Adds a Master connection to the game
	 * @param client the Master that wants to join the game.
	 * @return the PlayerEntity associated with the Master
	 */
	public PlayerEntity join(Master client) {
		PlayerEntity newPlayer = new PlayerEntity(world.getSpawn(), SpriteLoader.get("ghostBlue"), "marc");
		connections.put(client, newPlayer);
		getEntityTable(world.getSpawn().room).add(newPlayer);
		return newPlayer;
	}

	/**
	 * Removes a Master connection from the game
	 * @param client the Master that wants to leave the game
	 * @return true if the master was removed; false if nothing happened
	 */
	public boolean leave(Master client) {
		return connections.remove(client) != null;
	}

	// PLAYER ACTION

	public void move(Master m, Direction d) {
		PlayerEntity p = getPlayer(m);
		if (p != null) rules.move(p, d);
	}

	public void action(Master m, int selectedItem) {
		PlayerEntity p = getPlayer(m);
		rules.action(p, selectedItem);
	}


	public void drop(Master m, int selectedItem) {
		PlayerEntity p = getPlayer(m);
	}

	// GETTERS

	@Nullable
	public ObservableEntityTable<Entity> getEntityTable(Room r) {
		ObservableEntityTable<Entity> et = tables[r.worldRow][r.worldCol];
		return et != null ? et : null;
	}
/*
	@Nullable
	public Rules getRules(PlayerEntity p) {
		if (p == null) return null;
		Room r = p.getLocation().room;
		return rules[r.worldRow][r.worldRow];
	}
*/
	@Nullable
	public PlayerEntity getPlayer(Master client) {
		return connections.get(client);
	}

	/**
	 * Allows access to an Observer of the given Master's assigned PlayerEntity
	 * @param client the Client whose Player you want to fetch
	 * @return an Observable Object which relays all updates from the associated PlayerEntity
	 */
	@Nullable
	public Observable getPlayerObserver(Master client) {
		PlayerEntity p = connections.get(client);
		return p != null ? new Relay(p) : null;
	}

	/**
	 * Return an Observable which relays all updates from Entities in the given Room.
	 * @param r
	 * @return
	 */
	@Nullable
	public Observable getRoomObserver(Room r) {
		ObservableEntityTable<?> et = getEntityTable(r);
		return et != null ? new Relay(et) : null;
	}

	/**
	 * Get a Collection view of all Entities currently in the given Room.
	 * @param r
	 * @return
	 */
	// this method, annoyingly, undermines my protections against direct entity access by the Masters
	@Nullable
	public Collection<Entity> getRoomEntities(Room r) {
		Collection<Entity> s = getEntityTable(r);
		return s != null ? Collections.unmodifiableCollection(s) : null;
	}

	@Nullable
	public Room getRoom(Master m) {
		PlayerEntity p = connections.get(m);
		return p != null ? p.getLocation().room : null;
	}


	/**
	 * Wrapper class for Observable objects so that external classes can observe them without direct access.
	 */
	protected static class Relay extends Observable implements Observer {

		private final Observable target;

		public Relay(Observable target) {
			this.target = target;
		}

		@Override
		public void update(Observable o, Object arg) {
			if (o == target) {
				setChanged();
				notifyObservers(arg);
			}
		}
	}
}
