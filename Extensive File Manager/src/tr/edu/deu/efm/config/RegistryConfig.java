package tr.edu.deu.efm.config;

import tr.edu.deu.efm.command.api.CommandRegistry;
import tr.edu.deu.efm.command.impl.*;
import tr.edu.deu.efm.core.api.*;
import tr.edu.deu.efm.core.impl.archiver.DefaultArchiver;
import tr.edu.deu.efm.core.impl.changer.DefaultDirectoryChanger;
import tr.edu.deu.efm.core.impl.converter.MasterConverter;
import tr.edu.deu.efm.core.impl.copier.CopyCopier;
import tr.edu.deu.efm.core.impl.copier.OverwriteCopier;
import tr.edu.deu.efm.core.impl.creator.DefaultDirectoryCreator;
import tr.edu.deu.efm.core.impl.creator.DefaultFileCreator;
import tr.edu.deu.efm.core.impl.lister.DefaultLister;
import tr.edu.deu.efm.core.impl.mover.CopyMover;
import tr.edu.deu.efm.core.impl.mover.OverwriteMover;
import tr.edu.deu.efm.core.impl.opener.DefaultOpener;
import tr.edu.deu.efm.core.impl.remover.PermanentRemover;
import tr.edu.deu.efm.core.impl.remover.TrashBinRemover;
import tr.edu.deu.efm.core.impl.renamer.DefaultRenamer;

/**
 * Acts as the Composition Root for the EFM application, handling Dependency Injection (DI) 
 * and system initialization.
 * <p>
 * This configuration class is responsible for evaluating global {@link Settings}, instantiating 
 * the appropriate Core I/O workers (Strategy Pattern), and injecting them into their respective 
 * Command wrappers. Finally, it registers these fully constructed commands into the central 
 * routing registry.
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
     * <li>Evaluates global configuration flags (e.g., {@code removeSafely}, {@code overWriteMode}).</li>
     * <li>Instantiates the corresponding concrete implementations for Core interfaces.</li>
     * <li>Injects these core workers into Command layer instances.</li>
     * <li>Binds user-facing CLI triggers (e.g., "rm", "cp") to the fully assembled commands.</li>
     * </ol>
     * </p>
     *
     * @return A fully populated and ready-to-use {@link CommandRegistry} containing all 
     * valid terminal commands mapped to their execution logic.
     */
    public static CommandRegistry initializeCommandRegistry() {
        CommandRegistry registry = new DefaultCommandRegistry();
        
        EntityRemover deleter;
        EntityMover mover;
        EntityCopier copier;
        EntityLister lister;
        EntityRenamer renamer;
        DirectoryChanger directoryChanger;
        EntityCreator directoryCreator;
        EntityCreator fileCreator;
        EntityOpener opener;
        EntityCompressor compressor;
        EntityExtractor extractor;
        EntityConverter masterConverter;
        
        if(Settings.removeSafely) {
            deleter = new TrashBinRemover();
        } else {
            deleter = new PermanentRemover();
        }
        
        if(Settings.overWriteMode) {
            mover = new OverwriteMover();
            copier = new OverwriteCopier();
        } else {
            mover = new CopyMover();
            copier = new CopyCopier();
        }
                     
        lister = new DefaultLister();
        renamer = new DefaultRenamer();
        directoryChanger = new DefaultDirectoryChanger();
        directoryCreator = new DefaultDirectoryCreator();
        fileCreator = new DefaultFileCreator();
        opener = new DefaultOpener();
        compressor = new DefaultArchiver();
        extractor = new DefaultArchiver();
        masterConverter = new MasterConverter();
        
        registry.register("rm", new RmCommand(deleter));
        registry.register("mv", new MvCommand(mover));             
        registry.register("cp", new CpCommand(copier));
        registry.register("ls", new LsCommand(lister));
        registry.register("cd", new CdCommand(directoryChanger));
        registry.register("rn", new RnCommand(renamer));
        registry.register("mkdir", new MkdirCommand(directoryCreator));
        registry.register("touch", new TouchCommand(fileCreator));
        registry.register("open", new OpenCommand(opener));
        registry.register("zip", new ZipCommand(compressor));
        registry.register("unzip", new UnzipCommand(extractor));
        registry.register("convert", new ConvertCommand(masterConverter));

        registry.register("exit", new ExitCommand());
        registry.register("help", new HelpCommand(registry));
        registry.register("pwd", new PwdCommand());

        return registry;
    }
}