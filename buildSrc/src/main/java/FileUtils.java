import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Predicate;

/**
 * A utility class for working with files.
 */
@NoArgsConstructor
public final class FileUtils {

    /**
     * Deletes the target file if it exists.
     * Throws {@link FileException} in case of failure.
     *
     * @param file the file
     */
    public static void deleteIfExists(final @NotNull File file) {
        deleteIf(File::exists, file);
    }

    /**
     * Deletes the target file if the given predicate is verified.
     * Throws {@link FileException} in case of failure.
     *
     * @param predicate the predicate
     * @param target    the target
     */
    public static void deleteIf(final @NotNull Predicate<File> predicate,
                                final @NotNull File target) {
        if (predicate.test(target) && !target.delete())
            throw new FileException("delete", target);
    }

    private static @NotNull String getFileType(final @NotNull File file) {
        return file.isDirectory() ? "directory" : file.isFile() ? "file" : "none";
    }

    private static class FileException extends RuntimeException {

        /**
         * Instantiates a new File exception.
         *
         * @param message the message
         */
        public FileException(final @NotNull String message) {
            super(message);
        }

        /**
         * Instantiates a new File exception.
         * Generates a message of type "Failed to %action% %target_type% %target_path%"
         *
         * @param action the action
         * @param target the target
         */
        public FileException(final @NotNull String action, final @NotNull File target) {
            this(String.format("Failed to %s %s: %s", action, getFileType(target), target.getPath()));
        }

    }
}