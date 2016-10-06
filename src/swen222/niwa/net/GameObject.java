package swen222.niwa.net;

import com.sun.istack.internal.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Class allowing for synchronised object references between the client and server. Every game object should
 * extend from this class, and is assigned a unique identifier (uid) which is consistent between client and server.
 * Also provides utility for sending and receiving object data between server and client.
 *
 * @author Marc
 */
public class GameObject extends Observable {

	private static final Map<Integer, GameObject> map = new HashMap<>();
	private static int count = Integer.MIN_VALUE;

	public final int uid;

	/**
	 * Creates a new GameObject and assigns it a new UID. Should be used on server side only;
	 * clients should construct their objects through the InputStream constructor
	 */
	public GameObject() {
		this.uid = newUID();
		map.put(uid, this);
	}

	/**
	 * Constructs a GameObject from a DataInputStream. All subclasses should have a constructor
	 * of this format, for the client to deserialise new Objects sent by the server, which should then
	 * be chained back to this one.
	 *
	 * @param in DataInputStream with the result of an instances writeToStream()
	 * @throws IOException
	 */
	public GameObject(DataInputStream in) throws IOException {
		this.uid = in.readInt();
		if (map.put(uid, this) != null);
	}

	/**
	 * Writes the state of this GameObject to a DataOutputStream, such that the DataInputStream constructor,
	 * reading from the same stream, will create a clone. All subclasses' implementations of this method should
	 * first call the superclass method; this will stream the objects' data in the correct order for the the
	 * constructor chain to read it off.
	 *
	 * @param out DataOutputStream to write to.
	 * @throws IOException
	 */
	public void writeToStream(DataOutputStream out) throws IOException {
		out.write(uid);
	}

	/**
	 * Get a new UID to assign to a new GameObject. This should only be used by the server side constructors;
	 * clients should use the DataInputStream constructor, where the UID should be specified by the server.
	 * @return an integer that is not associated with any existing (and registered)
	 */
	private static int newUID() {
		return count++; // assumption: we will never reach (max int+1)*2 GameObjects, or anything near it
	}

	/**
	 * Get the GameObject associated with the UID
	 * @param uid of the GameObject to find
	 * @return the GameObject with the UID specified, null if it does not exist
	 */
	@Nullable
	public static GameObject get(int uid) {
		return map.get(uid);
	}

	/**
	 * Removes the GameObject associated with the UID from the table.
	 * Should be called where possible to allow garbage collection.
	 * @param uid of the GameObject to remove
	 * @return the GameObject that was removed, null if none with that UID existed
	 */
	@Nullable
	public static GameObject remove(int uid) {
		return map.remove(uid);
	}

	@Override
	protected void finalize() throws Throwable {
		remove(this.uid);
		super.finalize();
	}

	@Override
	public int hashCode() {
		return this.uid;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof GameObject && ((GameObject) obj).uid == this.uid);
	}
}
