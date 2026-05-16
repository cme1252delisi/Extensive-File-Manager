package tr.edu.deu.efm.config;

import tr.edu.deu.efm.command.api.CommandRegistry;
import tr.edu.deu.efm.command.api.ConsoleInteractor;
import tr.edu.deu.efm.command.impl.*;
import tr.edu.deu.efm.core.api.*;
import tr.edu.deu.efm.core.impl.archiver.ZIPCompressor;
import tr.edu.deu.efm.core.impl.archiver.ZIPExtractor;
import tr.edu.deu.efm.core.impl.changer.DefaultDirectoryChanger;
import tr.edu.deu.efm.core.impl.converter.MasterConverter;
import tr.edu.deu.efm.core.impl.copier.DefaultCopier;
import tr.edu.deu.efm.core.impl.creator.DefaultDirectoryCreator;
import tr.edu.deu.efm.core.impl.creator.DefaultFileCreator;
import tr.edu.deu.efm.core.impl.cryptographer.AESDecryptor;
import tr.edu.deu.efm.core.impl.cryptographer.AESEncryptor;
import tr.edu.deu.efm.core.impl.inspector.DefaultInspector;
import tr.edu.deu.efm.core.impl.lister.DefaultLister;
import tr.edu.deu.efm.core.impl.mover.DefaultMover;
import tr.edu.deu.efm.core.impl.opener.DefaultOpener;
import tr.edu.deu.efm.core.impl.remover.DefaultRemover;
import tr.edu.deu.efm.core.impl.renamer.DefaultRenamer;
import tr.edu.deu.efm.ui.impl.StandardConsoleAdapter;

/**
 * Acts as the Composition Root for the EFM application, handling Dependency
 * Injection (DI) and system initialization.
 * <p>
 * This configuration class is responsible for evaluating global
 * {@link Settings}, instantiating the appropriate Core I/O workers (Strategy
 * Pattern), and injecting them into their respective Command wrappers. Finally,
 * it registers these fully constructed commands into the central routing
 * registry.
 * </p>
 */
public class RegistryConfig {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private RegistryConfig() {

	}

	/**
	 * Bootstraps the application's command ecosystem.
	 * <p>
	 * <b>Initialization Flow:</b>
	 * <ol>
	 * <li>Evaluates global configuration flags (e.g., {@code removeSafely},
	 * {@code overWriteMode}).</li>
	 * <li>Instantiates the corresponding concrete implementations for Core
	 * interfaces.</li>
	 * <li>Injects these core workers into Command layer instances.</li>
	 * <li>Binds user-facing CLI triggers (e.g., "rm", "cp") to the fully assembled
	 * commands.</li>
	 * </ol>
	 * </p>
	 *
	 * @return A fully populated and ready-to-use {@link CommandRegistry} containing
	 *         all valid terminal commands mapped to their execution logic.
	 */
	public static CommandRegistry initializeCommandRegistry() {
		CommandRegistry registry = new DefaultCommandRegistry();

		EntityRemover remover = new DefaultRemover();
		EntityMover mover = new DefaultMover();
		EntityCopier copier = new DefaultCopier();
		EntityLister lister = new DefaultLister();
		EntityRenamer renamer = new DefaultRenamer();
		DirectoryChanger directoryChanger = new DefaultDirectoryChanger();
		EntityCreator directoryCreator = new DefaultDirectoryCreator();
		EntityCreator fileCreator = new DefaultFileCreator();
		EntityOpener opener = new DefaultOpener();
		EntityCompressor compressor = new ZIPCompressor();
		EntityExtractor extractor = new ZIPExtractor();
		EntityConverter converter = new MasterConverter();
		EntityEncryptor encryptor = new AESEncryptor();
		EntityDecryptor decryptor = new AESDecryptor();
		EntityInspector inspector = new DefaultInspector();
		ConsoleInteractor consoleAdapter = new StandardConsoleAdapter();

		registry.register("rm", new RmCommand(remover, consoleAdapter));
		registry.register("mv", new MvCommand(mover, consoleAdapter));
		registry.register("cp", new CpCommand(copier, consoleAdapter));
		registry.register("ls", new LsCommand(lister));
		registry.register("cd", new CdCommand(directoryChanger));
		registry.register("rn", new RnCommand(renamer, consoleAdapter));
		registry.register("mkdir", new MkdirCommand(directoryCreator));
		registry.register("touch", new TouchCommand(fileCreator));
		registry.register("open", new OpenCommand(opener));
		registry.register("zip", new ZipCommand(compressor, consoleAdapter));
		registry.register("unzip", new UnzipCommand(extractor, consoleAdapter));
		registry.register("convert", new ConvertCommand(converter, consoleAdapter));
		registry.register("encrypt", new EncryptCommand(encryptor, compressor, inspector, remover, renamer, consoleAdapter));
		registry.register("decrypt", new DecryptCommand(decryptor, extractor, inspector, remover, consoleAdapter));

		registry.register("exit", new ExitCommand());
		registry.register("help", new HelpCommand(registry));
		registry.register("pwd", new PwdCommand());
		registry.register("clearlog", new ClearLogCommand());

		return registry;
	}
}