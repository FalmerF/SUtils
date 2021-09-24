package net.smb.sutils.common;

import com.google.common.collect.Maps;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class ModuleResourcePack
implements IResourcePack {
    public static final Set<String> availableNamespaces = new HashSet<String>(Arrays.asList("moderutils"));
    private final Map<ResourceLocation, File> fileResources = Maps.newHashMap();
    private final File baseDir;

    public ModuleResourcePack(File dirPath) {
        this.baseDir = dirPath;
    }

    public InputStream getInputStream1(ResourceLocation location) throws IOException {
        File file = this.fileResources.get((Object)location);
        if (file != null && file.exists()) {
            return new FileInputStream(file);
        }
        InputStream jarInputStream = this.getInputStream(location);
        if (jarInputStream != null) {
            return jarInputStream;
        }
        throw new FileNotFoundException(location.getResourcePath());
    }

    public void addFileFor(ResourceLocation location) {
        String path = location.getResourcePath();
        path = path.substring(path.indexOf(47) + 1);
        this.addFileResource(location, new File(this.baseDir, path));
    }

    public void addFileResource(ResourceLocation location, File file) {
        this.fileResources.put(location, file);
    }

    protected static String getRelativePath(File left, File right) {
        return left.toURI().relativize(right.toURI()).getPath();
    }

	@Override
	public BufferedImage getPackImage() throws IOException {
		return null;
	}

	@Override
	public Set<String> getResourceDomains() {
		return availableNamespaces;
	}

	@Override
	public boolean resourceExists(ResourceLocation par1ResourceLocation) {
		try {
			return this.getInputStream(par1ResourceLocation) != null || this.fileResources.containsKey(par1ResourceLocation.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
		return ModuleResourcePack.class.getResourceAsStream("/assets/" + location.getResourceDomain() + "/" + location.getResourcePath());
	}

	@Override
	public String getPackName() {
		return "Moder Utils Module";
	}

	@Override
	public IMetadataSection getPackMetadata(IMetadataSerializer arg0, String arg1) throws IOException {
		return null;
	}
}

