package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for duplicating files and directories within the
 * file system.
 * <p>
 * This interface encapsulates the physical duplication logic, ensuring that
 * higher-level command layers remain agnostic to underlying I/O stream
 * operations and NIO.2 configurations.
 * </p>
 * <p>
 * <b>Strategy Pattern (Collision Policy):</b> Collision policies are resolved
 * through polymorphism. Specific concrete classes (such as
 * {@code OverwriteCopier} or {@code CopyCopier}) independently dictate the
 * exact behavior when a target already exists (e.g., overwriting vs. failing
 * gracefully).
 * </p>
 * <p>
 * <b>Recursive Deep Copy Policy:</b> Unlike standard shallow copy
 * implementations, when copying a directory, the implementation performs a full
 * recursive deep copy. It completely traverses the source directory tree and
 * duplicates all nested folders and files to the target destination while
 * perfectly maintaining the original hierarchical structure.
 * </p>
 */
public interface EntityCopier {

	/**
	 * Duplicates the entity located at the source path to the specified
	 * destination.
	 * <p>
	 * <b>Directory Handling:</b> If the target path points to an already existing
	 * directory, implementations copy the source entity <i>into</i> that directory.
	 * If the source is a directory, its entire contents are recursively copied.
	 * </p>
	 *
	 * @param currentDirectory The absolute path of the current working directory.
	 *                         This acts as the anchor for resolving relative paths
	 *                         correctly.
	 * @param sourcePath       The relative or absolute path of the entity to be
	 *                         copied.
	 * @param targetPath       The relative or absolute path of the destination.
	 * @return An {@link OperationResult} indicating the outcome.
	 */
	OperationResult copy(String currentDirectory, String sourcePath, String targetPath);
}