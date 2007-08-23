package entitymodeler.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.objectstyle.wolips.eomodeler.core.model.AbstractManifestEOModelGroupFactory;
import org.objectstyle.wolips.eomodeler.core.model.EOModelException;
import org.objectstyle.wolips.eomodeler.core.model.EOModelGroup;
import org.objectstyle.wolips.eomodeler.core.model.EOModelVerificationFailure;
import org.objectstyle.wolips.eomodeler.core.model.ManifestSearchFolder;

public class SimpleManifestEOModelGroupFactory extends AbstractManifestEOModelGroupFactory {
  @Override
  public List<ManifestSearchFolder> getSearchFolders(File selectedModelFolder) throws IOException {
    List<ManifestSearchFolder> searchFolders = new LinkedList<ManifestSearchFolder>();
    fillInSearchFolders(new File(selectedModelFolder, "EntityModeler.modelpath"), searchFolders);
    fillInSearchFolders(new File(selectedModelFolder, ".EntityModeler.modelpath"), searchFolders);
    fillInSearchFolders(new File(selectedModelFolder.getParentFile(), "EntityModeler.modelpath"), searchFolders);
    fillInSearchFolders(new File(selectedModelFolder.getParentFile(), ".EntityModeler.modelpath"), searchFolders);
    fillInSearchFolders(new File(System.getProperty("user.home"), "EntityModeler.modelpath"), searchFolders);
    fillInSearchFolders(new File(System.getProperty("user.home"), ".EntityModeler.modelpath"), searchFolders);
    fillInSearchFolders(new File(System.getProperty("user.home") + "/Library", "EntityModeler.modelpath"), searchFolders);
    fillInSearchFolders(new File(System.getProperty("user.home") + "/Library", ".EntityModeler.modelpath"), searchFolders);
    fillInSearchFolders(new File(System.getProperty("user.home") + "/Library/Preferences", "EntityModeler.modelpath"), searchFolders);
    fillInSearchFolders(new File(System.getProperty("user.home") + "/Library/Preferences", ".EntityModeler.modelpath"), searchFolders);
    return searchFolders;
  }

  public boolean canLoadModelGroupFrom(Object modelGroupResource) {
    return false;
  }

  @SuppressWarnings("unused")
  public EOModelGroup loadModelGroup(Object modelGroupResource, Set<EOModelVerificationFailure> failures, boolean skipOnDuplicates, URL editingModelURL, IProgressMonitor progressMonitor) throws EOModelException {
    return null;
  }

  protected void fillInSearchFolders(File manifestFile, List<ManifestSearchFolder> searchFolders) throws IOException {
    if (manifestFile.exists()) {
      BufferedReader manifestReader = new BufferedReader(new FileReader(manifestFile));
      try {
        String searchFolderPath;
        while ((searchFolderPath = manifestReader.readLine()) != null) {
          File searchFolder = new File(searchFolderPath).getAbsoluteFile();
          if (searchFolder != null && searchFolder.exists()) {
            searchFolders.add(new ManifestSearchFolder(searchFolder));
          }
        }
      }
      finally {
        manifestReader.close();
      }
    }
  }
}
