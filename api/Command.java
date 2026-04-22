package tr.edu.deu.efm.command.api;

/**
 * The central, unified contract for all user-facing commands within the Extensive File Manager (EFM).
 * <p>
 * This interface acts as an aggregate, extending both {@link Executable} and {@link Describable}.
 * By combining these two distinct responsibilities, it ensures that every valid command in the
 * system can not only perform its designated business logic but also provide its own 
 * metadata (name, description, and usage syntax) for dynamic menus and help systems.
 * </p>
 * <p>
 * <b>Architectural Note:</b> This interface intentionally declares no additional methods. 
 * It serves as a composite type, respecting the Interface Segregation Principle (ISP) 
 * while providing a single, convenient reference type for the {@code CommandRegistry} 
 * and the execution engine.
 * </p>
 */
public interface Command extends Executable, Describable {
}