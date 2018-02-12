package mobi.newsound.database;
import java.util.Optional;

/**
 * Created by Antonio Zaitoun on 11/02/2018.
 */

public class Column<T> extends Pair<String,Object> {

    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public Column(String key, Object value) {
        super(key, value);
    }

    /**
     * Optional Column of Type.
     *
     * Use this when the column is a foreign key and so your value might be null.
     *
     * @param key The name/key of the column.
     * @param value The optional value.
     * @param callback the callback that returns the value if the optional has a value.
     */
    public Column(String key, Optional<T> value, Value<T> callback) {
        super(key, value.isPresent() ? callback.getValue(value.get()) : null);
    }

    /**
     * The Value callback.
     * @param <T> The type of object we are sending in.
     */

    @FunctionalInterface
    public interface Value<T> {

        /**
         * Return the value for the given column from the object.
         * @param value The object
         * @return
         */
        Object getValue(T value);
    }

    /**
     * A method for the database to ignore the column if it has no value.
     * @return true if value is null.
     */
    public boolean shouldIgnore(){
        return getValue() == null;
    }

    /**
     * Get the key.
     * @return The key.
     */
    public String getKey(){
        return fst;
    }

    /**
     * Get the value.
     * @return The value.
     */
    public Object getValue(){
        return snd;
    }
}