package uk.ac.bbsrc.tgac.miso.dto;

import static uk.ac.bbsrc.tgac.miso.core.util.LimsUtils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.NotImplementedException;

import com.eaglegenomics.simlims.core.Group;
import com.eaglegenomics.simlims.core.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.bbsrc.tgac.miso.core.data.AbstractBoxPosition;
import uk.ac.bbsrc.tgac.miso.core.data.AbstractBoxable;
import uk.ac.bbsrc.tgac.miso.core.data.Array;
import uk.ac.bbsrc.tgac.miso.core.data.ArrayModel;
import uk.ac.bbsrc.tgac.miso.core.data.ArrayRun;
import uk.ac.bbsrc.tgac.miso.core.data.Box;
import uk.ac.bbsrc.tgac.miso.core.data.ChangeLog;
import uk.ac.bbsrc.tgac.miso.core.data.DetailedLibrary;
import uk.ac.bbsrc.tgac.miso.core.data.DetailedQcStatus;
import uk.ac.bbsrc.tgac.miso.core.data.DetailedSample;
import uk.ac.bbsrc.tgac.miso.core.data.Experiment;
import uk.ac.bbsrc.tgac.miso.core.data.Experiment.RunPartition;
import uk.ac.bbsrc.tgac.miso.core.data.IlluminaRun;
import uk.ac.bbsrc.tgac.miso.core.data.Index;
import uk.ac.bbsrc.tgac.miso.core.data.IndexFamily;
import uk.ac.bbsrc.tgac.miso.core.data.Institute;
import uk.ac.bbsrc.tgac.miso.core.data.Instrument;
import uk.ac.bbsrc.tgac.miso.core.data.InstrumentStatus;
import uk.ac.bbsrc.tgac.miso.core.data.Kit;
import uk.ac.bbsrc.tgac.miso.core.data.KitImpl;
import uk.ac.bbsrc.tgac.miso.core.data.LS454Run;
import uk.ac.bbsrc.tgac.miso.core.data.Lab;
import uk.ac.bbsrc.tgac.miso.core.data.Library;
import uk.ac.bbsrc.tgac.miso.core.data.LibraryDesign;
import uk.ac.bbsrc.tgac.miso.core.data.LibraryDesignCode;
import uk.ac.bbsrc.tgac.miso.core.data.LibraryQC;
import uk.ac.bbsrc.tgac.miso.core.data.Partition;
import uk.ac.bbsrc.tgac.miso.core.data.PartitionQCType;
import uk.ac.bbsrc.tgac.miso.core.data.Platform;
import uk.ac.bbsrc.tgac.miso.core.data.Pool;
import uk.ac.bbsrc.tgac.miso.core.data.PoolOrder;
import uk.ac.bbsrc.tgac.miso.core.data.PoolOrderCompletion;
import uk.ac.bbsrc.tgac.miso.core.data.PoolQC;
import uk.ac.bbsrc.tgac.miso.core.data.Printer;
import uk.ac.bbsrc.tgac.miso.core.data.Project;
import uk.ac.bbsrc.tgac.miso.core.data.QC;
import uk.ac.bbsrc.tgac.miso.core.data.ReferenceGenome;
import uk.ac.bbsrc.tgac.miso.core.data.Run;
import uk.ac.bbsrc.tgac.miso.core.data.Sample;
import uk.ac.bbsrc.tgac.miso.core.data.SampleAliquot;
import uk.ac.bbsrc.tgac.miso.core.data.SampleClass;
import uk.ac.bbsrc.tgac.miso.core.data.SampleGroupId;
import uk.ac.bbsrc.tgac.miso.core.data.SampleIdentity;
import uk.ac.bbsrc.tgac.miso.core.data.SampleLCMTube;
import uk.ac.bbsrc.tgac.miso.core.data.SampleNumberPerProject;
import uk.ac.bbsrc.tgac.miso.core.data.SamplePurpose;
import uk.ac.bbsrc.tgac.miso.core.data.SampleQC;
import uk.ac.bbsrc.tgac.miso.core.data.SampleSlide;
import uk.ac.bbsrc.tgac.miso.core.data.SampleStock;
import uk.ac.bbsrc.tgac.miso.core.data.SampleTissue;
import uk.ac.bbsrc.tgac.miso.core.data.SampleTissueProcessing;
import uk.ac.bbsrc.tgac.miso.core.data.SampleValidRelationship;
import uk.ac.bbsrc.tgac.miso.core.data.SequencerPartitionContainer;
import uk.ac.bbsrc.tgac.miso.core.data.SequencingParameters;
import uk.ac.bbsrc.tgac.miso.core.data.Stain;
import uk.ac.bbsrc.tgac.miso.core.data.Study;
import uk.ac.bbsrc.tgac.miso.core.data.StudyType;
import uk.ac.bbsrc.tgac.miso.core.data.Submission;
import uk.ac.bbsrc.tgac.miso.core.data.Subproject;
import uk.ac.bbsrc.tgac.miso.core.data.TissueMaterial;
import uk.ac.bbsrc.tgac.miso.core.data.TissueOrigin;
import uk.ac.bbsrc.tgac.miso.core.data.TissueType;
import uk.ac.bbsrc.tgac.miso.core.data.impl.BoxImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.ContainerQC;
import uk.ac.bbsrc.tgac.miso.core.data.impl.Deletion;
import uk.ac.bbsrc.tgac.miso.core.data.impl.DetailedLibraryImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.DetailedLibraryTemplate;
import uk.ac.bbsrc.tgac.miso.core.data.impl.DetailedQcStatusImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.DetailedSampleImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.InstituteImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.InstrumentImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.LabImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.LibraryDilution;
import uk.ac.bbsrc.tgac.miso.core.data.impl.LibraryImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.LibraryTemplate;
import uk.ac.bbsrc.tgac.miso.core.data.impl.PartitionImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.PoolImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.PoolOrderImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.ProjectImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleAliquotImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleClassImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleGroupImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleIdentityImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleLCMTubeImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleNumberPerProjectImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SamplePurposeImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleSlideImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleStockImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleTissueImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleTissueProcessingImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SampleValidRelationshipImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SequencerPartitionContainerImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SequencingContainerModel;
import uk.ac.bbsrc.tgac.miso.core.data.impl.StorageLocation;
import uk.ac.bbsrc.tgac.miso.core.data.impl.StorageLocation.LocationUnit;
import uk.ac.bbsrc.tgac.miso.core.data.impl.StudyImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SubprojectImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.TargetedSequencing;
import uk.ac.bbsrc.tgac.miso.core.data.impl.TissueMaterialImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.TissueOriginImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.TissueTypeImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.boxposition.DilutionBoxPosition;
import uk.ac.bbsrc.tgac.miso.core.data.impl.boxposition.LibraryBoxPosition;
import uk.ac.bbsrc.tgac.miso.core.data.impl.boxposition.PoolBoxPosition;
import uk.ac.bbsrc.tgac.miso.core.data.impl.boxposition.SampleBoxPosition;
import uk.ac.bbsrc.tgac.miso.core.data.impl.kit.KitDescriptor;
import uk.ac.bbsrc.tgac.miso.core.data.impl.view.BarcodableView;
import uk.ac.bbsrc.tgac.miso.core.data.impl.view.BoxableView;
import uk.ac.bbsrc.tgac.miso.core.data.impl.view.PoolableElementView;
import uk.ac.bbsrc.tgac.miso.core.data.spreadsheet.SpreadSheetFormat;
import uk.ac.bbsrc.tgac.miso.core.data.spreadsheet.Spreadsheet;
import uk.ac.bbsrc.tgac.miso.core.data.type.ConsentLevel;
import uk.ac.bbsrc.tgac.miso.core.data.type.HealthType;
import uk.ac.bbsrc.tgac.miso.core.data.type.InstrumentType;
import uk.ac.bbsrc.tgac.miso.core.data.type.KitType;
import uk.ac.bbsrc.tgac.miso.core.data.type.LibrarySelectionType;
import uk.ac.bbsrc.tgac.miso.core.data.type.LibraryStrategyType;
import uk.ac.bbsrc.tgac.miso.core.data.type.LibraryType;
import uk.ac.bbsrc.tgac.miso.core.data.type.PlatformType;
import uk.ac.bbsrc.tgac.miso.core.data.type.QcType;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow.WorkflowName;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;
import uk.ac.bbsrc.tgac.miso.core.service.printing.Backend;
import uk.ac.bbsrc.tgac.miso.core.service.printing.Driver;
import uk.ac.bbsrc.tgac.miso.core.util.BoxUtils;
import uk.ac.bbsrc.tgac.miso.core.util.LimsUtils;

@SuppressWarnings("squid:S3776") // make Sonar ignore cognitive complexity warnings for this file
public class Dtos {

  public static TissueOriginDto asDto(TissueOrigin from) {
    TissueOriginDto dto = new TissueOriginDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    dto.setDescription(from.getDescription());
    dto.setLabel(from.getItemLabel());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    return dto;
  }

  public static Set<TissueOriginDto> asTissueOriginDtos(Set<TissueOrigin> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static TissueOrigin to(TissueOriginDto from) {
    TissueOrigin to = new TissueOriginImpl();
    if (from.getId() != null) to.setId(from.getId());
    to.setAlias(from.getAlias());
    to.setDescription(from.getDescription());
    return to;
  }

  public static TissueTypeDto asDto(TissueType from) {
    TissueTypeDto dto = new TissueTypeDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    dto.setDescription(from.getDescription());
    dto.setLabel(from.getItemLabel());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    return dto;
  }

  public static Set<TissueTypeDto> asTissueTypeDtos(Set<TissueType> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static TissueType to(TissueTypeDto from) {
    TissueType to = new TissueTypeImpl();
    if (from.getId() != null) to.setId(from.getId());
    to.setAlias(from.getAlias());
    to.setDescription(from.getDescription());
    return to;
  }

  public static SubprojectDto asDto(Subproject from) {
    SubprojectDto dto = new SubprojectDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    dto.setDescription(from.getDescription());
    dto.setPriority(from.getPriority());
    dto.setParentProjectId(from.getParentProject().getId());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    dto.setReferenceGenomeId(from.getReferenceGenomeId());
    return dto;
  }

  public static Set<SubprojectDto> asSubprojectDtos(Set<Subproject> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static Subproject to(SubprojectDto from) {
    Subproject to = new SubprojectImpl();
    to.setAlias(from.getAlias());
    to.setDescription(from.getDescription());
    to.setPriority(from.getPriority());
    to.setReferenceGenomeId(from.getReferenceGenomeId());
    return to;
  }

  public static SampleClassDto asDto(SampleClass from) {
    SampleClassDto dto = new SampleClassDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    dto.setSampleCategory(from.getSampleCategory());
    dto.setSuffix(from.getSuffix());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    dto.setDNAseTreatable(from.getDNAseTreatable());
    return dto;
  }

  public static Set<SampleClassDto> asSampleClassDtos(Set<SampleClass> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static SampleClass to(SampleClassDto from) {
    SampleClass to = new SampleClassImpl();
    to.setAlias(from.getAlias());
    to.setSampleCategory(from.getSampleCategory());
    to.setSuffix(from.getSuffix());
    to.setDNAseTreatable(from.getDNAseTreatable());
    return to;
  }

  public static DetailedQcStatusDto asDto(DetailedQcStatus from) {
    DetailedQcStatusDto dto = new DetailedQcStatusDto();
    dto.setId(from.getId());
    dto.setStatus(from.getStatus());
    dto.setDescription(from.getDescription());
    dto.setNoteRequired(from.getNoteRequired());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    return dto;
  }

  public static Set<DetailedQcStatusDto> asDetailedQcStatusDtos(Set<DetailedQcStatus> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static DetailedQcStatus to(DetailedQcStatusDto from) {
    DetailedQcStatus to = new DetailedQcStatusImpl();
    to.setStatus(from.getStatus());
    to.setDescription(from.getDescription());
    to.setNoteRequired(from.isNoteRequired());
    return to;
  }

  public static SampleDto asMinimalDto(Sample from) {
    DetailedSampleDto dto = new DetailedSampleDto();
    copySampleFields(from, dto, false);

    if (isDetailedSample(from)) {
      DetailedSample detailed = (DetailedSample) from;
      dto.setSampleClassId(detailed.getSampleClass().getId());
      dto.setIdentityConsentLevel(getIdentityConsentLevelString(detailed));
    }
    return dto;
  }

  private static SampleDto copySampleFields(Sample from, SampleDto dto, boolean includeBoxables) {
    dto.setId(from.getId());
    dto.setName(from.getName());
    dto.setDescription(from.getDescription());
    dto.setUpdatedById(from.getLastModifier().getUserId());
    dto.setIdentificationBarcode(from.getIdentificationBarcode());
    dto.setLocationBarcode(from.getLocationBarcode());
    dto.setLocationLabel(BoxUtils.makeLocationLabel(from));
    if (from.getBox() != null) {
      dto.setBox(asDto(from.getBox(), includeBoxables));
      dto.setBoxPosition(from.getBoxPosition());
    }
    dto.setSampleType(from.getSampleType());
    dto.setReceivedDate(from.getReceivedDate() == null ? null : formatDate(from.getReceivedDate()));
    if (from.getQcPassed() != null) {
      dto.setQcPassed(from.getQcPassed());
    }
    dto.setAlias(from.getAlias());
    dto.setProjectId(from.getProject().getId());
    dto.setScientificName(from.getScientificName());
    dto.setTaxonIdentifier(from.getTaxonIdentifier());
    if (from.getVolume() != null) {
      dto.setVolume(from.getVolume().toString());
    }
    dto.setDiscarded(from.isDiscarded());
    dto.setLastModified(formatDateTime(from.getLastModified()));

    return dto;

  }

  private static DetailedSampleDto asDetailedSampleDto(DetailedSample from) {
    DetailedSampleDto dto = null;
    if (isIdentitySample(from)) {
      dto = asIdentitySampleDto((SampleIdentity) from);
    } else if (isTissueSample(from)) {
      dto = asTissueSampleDto((SampleTissue) from);
    } else if (isTissueProcessingSample(from)) {
      dto = asTissueProcessingSampleDto((SampleTissueProcessing) from);
    } else if (isAliquotSample(from)) {
      dto = asAliquotSampleDto((SampleAliquot) from);
    } else if (isStockSample(from)) {
      dto = asStockSampleDto((SampleStock) from);
    } else {
      throw new IllegalArgumentException();
    }
    dto.setSampleClassId(from.getSampleClass().getId());
    if (from.getDetailedQcStatus() != null) {
      dto.setDetailedQcStatusId(from.getDetailedQcStatus().getId());
    }
    if (from.getSubproject() != null) {
      dto.setSubprojectId(from.getSubproject().getId());
    }
    if (from.getParent() != null) {
      dto.setParentId(from.getParent().getId());
      dto.setParentAlias(from.getParent().getAlias());
      dto.setParentTissueSampleClassId(from.getParent().getSampleClass().getId());
      dto.setIdentityConsentLevel(getIdentityConsentLevelString(from));
    }
    if (from.getGroupId() != null) {
      dto.setGroupId(from.getGroupId());
    }
    if (from.getGroupDescription() != null) {
      dto.setGroupDescription(from.getGroupDescription());
    }
    if (from.isSynthetic() != null) {
      dto.setSynthetic(from.isSynthetic());
    }
    dto.setConcentration(from.getConcentration() == null ? null : from.getConcentration().toString());
    dto.setNonStandardAlias(from.hasNonStandardAlias());
    if (from.getDetailedQcStatus() != null) {
      dto.setDetailedQcStatusId(from.getDetailedQcStatus().getId());
    }
    dto.setDetailedQcStatusNote(from.getDetailedQcStatusNote());
    return dto;
  }

  private static DetailedSample toDetailedSample(DetailedSampleDto from) {
    DetailedSample to = null;
    if (from.getClass() == SampleIdentityDto.class) {
      to = toIdentitySample((SampleIdentityDto) from);
    } else if (from.getClass() == SampleTissueDto.class) {
      to = toTissueSample((SampleTissueDto) from);
    } else if (from instanceof SampleTissueProcessingDto) {
      to = toTissueProcessingSample((SampleTissueProcessingDto) from);
    } else if (from.getClass() == SampleStockDto.class) {
      to = toStockSample((SampleStockDto) from);
    } else if (from.getClass() == SampleAliquotDto.class) {
      to = toAliquotSample((SampleAliquotDto) from);
    } else {
      to = new DetailedSampleImpl();
    }
    if (from.getDetailedQcStatusId() != null) {
      DetailedQcStatus detailedQcStatus = new DetailedQcStatusImpl();
      detailedQcStatus.setId(from.getDetailedQcStatusId());
      to.setDetailedQcStatus(detailedQcStatus);
    }
    to.setDetailedQcStatusNote(from.getDetailedQcStatusNote());
    if (from.getSubprojectId() != null) {
      Subproject subproject = new SubprojectImpl();
      subproject.setId(from.getSubprojectId());
      to.setSubproject(subproject);
    }
    if (from.getSampleClassId() != null) {
      SampleClass sampleClass = new SampleClassImpl();
      sampleClass.setId(from.getSampleClassId());
      to.setSampleClass(sampleClass);
    }
    to.setGroupId(nullifyStringIfBlank(from.getGroupId()));
    to.setGroupDescription(nullifyStringIfBlank(from.getGroupDescription()));
    to.setSynthetic(from.getSynthetic());
    to.setConcentration(from.getConcentration() == null ? null : Double.valueOf(from.getConcentration()));
    if (from.getIdentityId() != null) {
      to.setIdentityId(from.getIdentityId());
    }
    to.setNonStandardAlias(from.getNonStandardAlias());
    to.setParent(getParent(from));
    return to;
  }

  /**
   * Extracts parent details from the DTO, according to these possible cases:
   *
   * <ol>
   * <li>parent ID is provided. This implies that the parent exists, so no other parent information will be required</li>
   * <li>identity information and parentTissueSampleClassId are provided. This implies that a tissue parent should be created, and that the
   * identity may or may not yet exist. If the sampleClassId is an aliquot, a stockClassId must be provided. ParentAliquotClassId may be
   * provided to indicate a second aliquot level in the hierarchy</li>
   * <li>identity information is provided, but no parentTissueSampleClassId. You must be creating a tissue in this case.</li>
   * </ol>
   *
   * @param childDto
   *          the DTO to take parent details from
   * @return the parent details from the DTO, or null if there are none. A returned sample will also include its own parent if applicable.
   */
  private static DetailedSample getParent(DetailedSampleDto childDto) {
    DetailedSample parent = null;
    if (childDto.getParentId() != null) {
      parent = new DetailedSampleImpl();
      parent.setId(childDto.getParentId());
    } else {
      if (childDto instanceof SampleIdentityDto && childDto.getClass() != SampleIdentityDto.class) {
        parent = toIdentitySample((SampleIdentityDto) childDto);
      }

      if (childDto instanceof SampleTissueDto && childDto.getClass() != SampleTissueDto.class) {
        if (childDto.getParentTissueSampleClassId() == null) {
          throw new IllegalArgumentException("No tissue class specified.");
        }
        DetailedSample tissue = toTissueSample((SampleTissueDto) childDto);
        tissue.setSampleClass(new SampleClassImpl());
        tissue.getSampleClass().setId(childDto.getParentTissueSampleClassId());
        tissue.setParent(parent);
        parent = tissue;

        if (childDto instanceof SampleLCMTubeDto) {
          SampleLCMTubeDto lcm = (SampleLCMTubeDto) childDto;
          if (lcm.getParentSlideClassId() != null) {
            SampleSlide slide = new SampleSlideImpl();
            slide.setSampleClass(new SampleClassImpl());
            slide.getSampleClass().setId(lcm.getParentSlideClassId());
            slide.setSlides(0);
            slide.setParent(parent);
            parent = slide;
          }
        }
      }
      if (childDto instanceof SampleStockDto && childDto.getClass() != SampleStockDto.class) {
        SampleAliquotDto aliquotDto = (SampleAliquotDto) childDto;
        DetailedSample stock = toStockSample((SampleStockDto) childDto);
        stock.setSampleClass(new SampleClassImpl());
        stock.getSampleClass().setId(aliquotDto.getStockClassId());
        stock.setParent(parent);
        parent = stock;

        if (aliquotDto.getParentAliquotClassId() != null) {
          DetailedSample parentAliquot = toAliquotSample(aliquotDto);
          parentAliquot.setSampleClass(new SampleClassImpl());
          parentAliquot.getSampleClass().setId(aliquotDto.getParentAliquotClassId());
          parentAliquot.setParent(parent);
          parent = parentAliquot;
        }
      }
    }
    return parent;
  }

  public static TissueMaterialDto asDto(TissueMaterial from) {
    TissueMaterialDto dto = new TissueMaterialDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    return dto;
  }

  public static Set<TissueMaterialDto> asTissueMaterialDtos(Set<TissueMaterial> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static TissueMaterial to(TissueMaterialDto from) {
    TissueMaterial to = new TissueMaterialImpl();
    to.setAlias(from.getAlias());
    return to;
  }

  public static SamplePurposeDto asDto(SamplePurpose from) {
    SamplePurposeDto dto = new SamplePurposeDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    return dto;
  }

  public static Set<SamplePurposeDto> asSamplePurposeDtos(Set<SamplePurpose> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static SamplePurpose to(SamplePurposeDto from) {
    SamplePurpose to = new SamplePurposeImpl();
    to.setAlias(from.getAlias());
    return to;
  }

  public static SampleGroupDto asDto(SampleGroupId from) {
    SampleGroupDto dto = new SampleGroupDto();
    dto.setId(from.getId());
    dto.setGroupId(from.getGroupId());
    dto.setProjectId(from.getProject().getId());
    dto.setSubprojectId(from.getSubproject() == null ? null : from.getSubproject().getId());
    dto.setDescription(from.getDescription());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    return dto;
  }

  public static Set<SampleGroupDto> asSampleGroupDtos(Set<SampleGroupId> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static SampleGroupId to(SampleGroupDto from) {
    SampleGroupId to = new SampleGroupImpl();
    to.setGroupId(from.getGroupId());
    to.setDescription(from.getDescription());
    return to;
  }

  private static SampleAliquotDto asAliquotSampleDto(SampleAliquot from) {
    SampleAliquotDto dto = new SampleAliquotDto();
    if (from.getSamplePurpose() != null) {
      dto.setSamplePurposeId(from.getSamplePurpose().getId());
    }
    return dto;
  }

  private static SampleStockDto asStockSampleDto(SampleStock from) {
    SampleStockDto dto = new SampleStockDto();
    dto.setStrStatus(from.getStrStatus().getLabel());
    dto.setDnaseTreated(from.getDNAseTreated());
    return dto;
  }

  private static SampleStock toStockSample(SampleStockDto from) {
    SampleStock to = new SampleStockImpl();
    if (from.getStrStatus() != null) {
      to.setStrStatus(from.getStrStatus());
    }
    to.setDNAseTreated(from.getDnaseTreated());
    return to;
  }

  private static SampleAliquot toAliquotSample(SampleAliquotDto from) {
    SampleAliquot to = new SampleAliquotImpl();
    if (from.getSamplePurposeId() != null) {
      to.setSamplePurpose(new SamplePurposeImpl());
      to.getSamplePurpose().setId(from.getSamplePurposeId());
    }
    return to;
  }

  public static SampleDto asDto(Sample from) {
    SampleDto dto = null;

    if (isDetailedSample(from)) {
      dto = asDetailedSampleDto((DetailedSample) from);
    } else {
      dto = new SampleDto();
    }
    copySampleFields(from, dto, true);
    dto.setAccession(from.getAccession());

    if (from.getQCs() != null && !from.getQCs().isEmpty()) {
      dto.setQcs(asQcDtos(from.getQCs()));
    }
    return dto;
  }

  public static List<SampleDto> asSampleDtos(Collection<Sample> from, boolean full) {
    return from.stream().map(sample -> (full ? asDto(sample) : asMinimalDto(sample))).collect(Collectors.toList());
  }

  public static Sample to(SampleDto from) {
    Sample to = null;
    if (from instanceof DetailedSampleDto) {
      to = toDetailedSample((DetailedSampleDto) from);
    } else {
      to = new SampleImpl();
    }

    if (from.getId() != null) to.setId(from.getId());
    to.setAccession(nullifyStringIfBlank(from.getAccession()));
    to.setName(from.getName());
    to.setDescription(nullifyStringIfBlank(from.getDescription()));
    to.setIdentificationBarcode(nullifyStringIfBlank(from.getIdentificationBarcode()));
    to.setLocationBarcode(nullifyStringIfBlank(from.getLocationBarcode()));
    to.setSampleType(from.getSampleType());
    to.setReceivedDate(parseDate(from.getReceivedDate()));
    to.setQcPassed(from.getQcPassed());
    to.setScientificName(from.getScientificName());
    to.setTaxonIdentifier(from.getTaxonIdentifier());
    to.setAlias(from.getAlias());
    to.setDescription(from.getDescription());
    to.setVolume(isStringEmptyOrNull(from.getVolume()) ? null : Double.valueOf(from.getVolume()));
    to.setDiscarded(from.isDiscarded());
    if (from.getProjectId() != null) {
      to.setProject(new ProjectImpl());
      to.getProject().setId(from.getProjectId());
    }
    to.setBoxPosition((SampleBoxPosition) makeBoxablePosition(from, (SampleImpl) to));
    return to;
  }

  private static <T extends AbstractBoxableDto, U extends AbstractBoxable> AbstractBoxPosition makeBoxablePosition(T from, U to) {
    if (from.getBox() != null && (from.getBox().getId() != null || !isStringEmptyOrNull(from.getBoxPosition()))) {
      AbstractBoxPosition bp = to.getEntityType().makeBoxPosition();
      bp.setBox(to(from.getBox()));
      bp.setPosition(from.getBoxPosition());
      return bp;
    }
    return null;
  }

  private static SampleIdentityDto asIdentitySampleDto(SampleIdentity from) {
    SampleIdentityDto dto = new SampleIdentityDto();
    dto.setExternalName(from.getExternalName());
    dto.setDonorSex(from.getDonorSex().getLabel());
    if (from.getConsentLevel() != null) {
      dto.setConsentLevel(from.getConsentLevel().getLabel());
      // set here too, so it can be checked consistently for all DetailedSampleDtos
      dto.setIdentityConsentLevel(from.getConsentLevel().getLabel());
    }
    return dto;
  }

  private static SampleIdentity toIdentitySample(SampleIdentityDto from) {
    SampleIdentity to = new SampleIdentityImpl();
    to.setExternalName(from.getExternalName());
    if (from.getDonorSex() != null) {
      to.setDonorSex(from.getDonorSex());
    }
    if (from.getConsentLevel() != null) {
      to.setConsentLevel(ConsentLevel.getByLabel(from.getConsentLevel()));
    }
    return to;
  }

  public static SampleNumberPerProjectDto asDto(SampleNumberPerProject from) {
    SampleNumberPerProjectDto dto = new SampleNumberPerProjectDto();
    dto.setId(from.getId());
    dto.setProjectId(from.getProject().getId());
    dto.setHighestSampleNumber(from.getHighestSampleNumber());
    dto.setPadding(from.getPadding());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    return dto;
  }

  public static Set<SampleNumberPerProjectDto> asSampleNumberPerProjectDtos(Set<SampleNumberPerProject> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static SampleNumberPerProject to(SampleNumberPerProjectDto from) {
    SampleNumberPerProject to = new SampleNumberPerProjectImpl();
    to.setHighestSampleNumber(from.getHighestSampleNumber());
    to.setPadding(from.getPadding());
    return to;
  }

  public static SampleValidRelationshipDto asDto(SampleValidRelationship from) {
    SampleValidRelationshipDto dto = new SampleValidRelationshipDto();
    dto.setId(from.getId());
    dto.setParentId(from.getParent().getId());
    dto.setChildId(from.getChild().getId());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    dto.setArchived(from.getArchived());
    return dto;
  }

  public static Set<SampleValidRelationshipDto> asSampleValidRelationshipDtos(Set<SampleValidRelationship> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static SampleValidRelationship to(SampleValidRelationshipDto from) {
    SampleValidRelationship to = new SampleValidRelationshipImpl();
    return to;
  }

  public static InstituteDto asDto(Institute from) {
    InstituteDto dto = new InstituteDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    return dto;
  }

  public static Set<InstituteDto> asInstituteDtos(Set<Institute> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static Institute to(InstituteDto from) {
    Institute to = new InstituteImpl();
    to.setAlias(from.getAlias());
    return to;
  }

  public static LabDto asDto(Lab from) {
    LabDto dto = new LabDto();
    dto.setId(from.getId());
    dto.setInstituteId(from.getInstitute().getId());
    dto.setInstituteAlias(from.getInstitute().getAlias());
    dto.setAlias(from.getAlias());
    dto.setLabel(from.getItemLabel());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    return dto;
  }

  public static Set<LabDto> asLabDtos(Collection<Lab> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static Lab to(LabDto from) {
    Lab to = new LabImpl();
    to.setAlias(from.getAlias());
    return to;
  }

  private static SampleTissueDto asTissueSampleDto(SampleTissue from) {
    SampleTissueDto dto = new SampleTissueDto();
    dto.setPassageNumber(from.getPassageNumber());
    dto.setTimesReceived(from.getTimesReceived());
    dto.setSecondaryIdentifier(from.getSecondaryIdentifier());
    dto.setRegion(from.getRegion());
    dto.setTubeNumber(from.getTubeNumber());
    if (from.getLab() != null) {
      dto.setLabId(from.getLab().getId());
    }
    if (from.getTissueOrigin() != null) {
      dto.setTissueOriginId(from.getTissueOrigin().getId());
    }
    if (from.getTissueType() != null) {
      dto.setTissueTypeId(from.getTissueType().getId());
    }
    if (from.getTissueMaterial() != null) {
      dto.setTissueMaterialId(from.getTissueMaterial().getId());
    }
    return dto;
  }

  private static SampleTissue toTissueSample(SampleTissueDto from) {
    SampleTissue to = new SampleTissueImpl();
    to.setPassageNumber(from.getPassageNumber());
    to.setTimesReceived(from.getTimesReceived());
    to.setTubeNumber(from.getTubeNumber());
    to.setRegion(nullifyStringIfBlank(from.getRegion()));
    to.setSecondaryIdentifier(from.getSecondaryIdentifier());
    if (from.getTissueOriginId() != null) {
      TissueOrigin tissueOrigin = new TissueOriginImpl();
      tissueOrigin.setId(from.getTissueOriginId());
      to.setTissueOrigin(tissueOrigin);
    }
    if (from.getTissueTypeId() != null) {
      TissueType tissueType = new TissueTypeImpl();
      tissueType.setId(from.getTissueTypeId());
      to.setTissueType(tissueType);
    }
    if (from.getTissueMaterialId() != null) {
      TissueMaterial tissueMaterial = new TissueMaterialImpl();
      tissueMaterial.setId(from.getTissueMaterialId());
      to.setTissueMaterial(tissueMaterial);
    }
    if (from.getLabId() != null) {
      Lab lab = new LabImpl();
      lab.setId(from.getLabId());
      to.setLab(lab);
    }
    return to;
  }

  private static SampleTissueProcessingDto asTissueProcessingSampleDto(SampleTissueProcessing from) {
    SampleTissueProcessingDto dto = null;
    from = deproxify(from);

    if (from instanceof SampleSlideImpl) {
      dto = asSlideSampleDto((SampleSlide) from);
    } else if (from.getClass() == SampleLCMTubeImpl.class) {
      dto = asLCMTubeSampleDto((SampleLCMTube) from);
    } else {
      dto = new SampleTissueProcessingDto();
    }
    return dto;
  }

  private static SampleTissueProcessing toTissueProcessingSample(SampleTissueProcessingDto from) {
    SampleTissueProcessing to = null;
    if (from.getClass() == SampleSlideDto.class) {
      to = toSlideSample((SampleSlideDto) from);
    } else if (from.getClass() == SampleLCMTubeDto.class) {
      to = toLCMTubeSample((SampleLCMTubeDto) from);
    } else {
      to = new SampleTissueProcessingImpl();
    }
    return to;
  }

  private static SampleSlideDto asSlideSampleDto(SampleSlide from) {
    SampleSlideDto dto = new SampleSlideDto();
    dto.setSlides(from.getSlides());
    dto.setDiscards(from.getDiscards());
    dto.setSlidesRemaining(from.getSlidesRemaining());
    dto.setThickness(from.getThickness());
    dto.setStain(from.getStain() == null ? null : asDto(from.getStain()));
    return dto;
  }

  private static SampleSlide toSlideSample(SampleSlideDto from) {
    SampleSlide to = new SampleSlideImpl();
    to.setSlides(from.getSlides());
    to.setDiscards(from.getDiscards());
    to.setThickness(from.getThickness());
    if (from.getStain() == null) {
      to.setStain(null);
    } else {
      Stain stain = new Stain();
      stain.setId(from.getStain().getId());
      to.setStain(stain);
    }
    return to;
  }

  private static SampleLCMTubeDto asLCMTubeSampleDto(SampleLCMTube from) {
    SampleLCMTubeDto dto = new SampleLCMTubeDto();
    dto.setSlidesConsumed(from.getSlidesConsumed());
    return dto;
  }

  private static SampleLCMTube toLCMTubeSample(SampleLCMTubeDto from) {
    SampleLCMTube to = new SampleLCMTubeImpl();
    to.setSlidesConsumed(from.getSlidesConsumed());
    return to;
  }

  public static KitDescriptorDto asDto(KitDescriptor from) {
    KitDescriptorDto dto = new KitDescriptorDto();
    dto.setId(from.getId());
    dto.setName(from.getName());
    dto.setManufacturer(from.getManufacturer());
    dto.setPartNumber(from.getPartNumber());
    dto.setVersion(from.getVersion());
    dto.setStockLevel(from.getStockLevel());
    dto.setKitType(from.getKitType().getKey());
    dto.setPlatformType(from.getPlatformType().getKey());
    return dto;
  }

  public static Set<KitDescriptorDto> asKitDescriptorDtos(Collection<KitDescriptor> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static KitDescriptor to(KitDescriptorDto from) {
    KitDescriptor to = new KitDescriptor();
    if (from.getId() != null) to.setId(from.getId());
    to.setName(from.getName());
    to.setManufacturer(from.getManufacturer());
    to.setPartNumber(from.getPartNumber());
    to.setVersion(from.getVersion());
    to.setStockLevel(from.getStockLevel());
    to.setKitType(KitType.get(from.getKitType()));
    to.setPlatformType(PlatformType.get(from.getPlatformType()));
    return to;
  }

  public static LibraryDesignCodeDto asDto(LibraryDesignCode from) {
    LibraryDesignCodeDto dto = new LibraryDesignCodeDto();
    dto.setId(from.getId());
    dto.setCode(from.getCode());
    dto.setDescription(from.getDescription());
    dto.setTargetedSequencingRequired(from.isTargetedSequencingRequired());
    return dto;
  }

  public static LibraryDesignCode to(LibraryDesignCodeDto from) {
    LibraryDesignCode to = new LibraryDesignCode();
    if (from.getId() != null) to.setId(from.getId());
    to.setCode(from.getCode());
    to.setDescription(from.getDescription());
    to.setTargetedSequencingRequired(from.isTargetedSequencingRequired());
    return to;
  }

  private static DetailedLibraryDto asDetailedLibraryDto(DetailedLibrary from) {
    DetailedLibraryDto dto = new DetailedLibraryDto();
    if (from.getLibraryDesign() != null) {
      dto.setLibraryDesignId(from.getLibraryDesign().getId());
    }
    dto.setLibraryDesignCodeId(from.getLibraryDesignCode().getId());
    dto.setPreMigrationId(from.getPreMigrationId());
    dto.setArchived(from.getArchived());
    dto.setNonStandardAlias(from.hasNonStandardAlias());
    if (from.getGroupId() != null) {
      dto.setGroupId(from.getGroupId());
    }
    if (from.getGroupDescription() != null) {
      dto.setGroupDescription(from.getGroupDescription());
    }
    if (from.getSample() != null) {
      dto.setIdentityConsentLevel(getIdentityConsentLevelString((DetailedSample) from.getSample()));
    }
    return dto;
  }

  private static String getIdentityConsentLevelString(DetailedSample sample) {
    ConsentLevel level = getIdentityConsentLevel(sample);
    return level == null ? null : level.getLabel();
  }

  public static DetailedLibrary toDetailedLibrary(DetailedLibraryDto from) {
    if (from == null) return null;
    DetailedLibrary to = new DetailedLibraryImpl();
    if (from.getLibraryDesignId() != null) {
      LibraryDesign design = new LibraryDesign();
      design.setId(from.getLibraryDesignId());
      to.setLibraryDesign(design);
    }
    LibraryDesignCode ldCode = new LibraryDesignCode();
    ldCode.setId(from.getLibraryDesignCodeId());
    to.setLibraryDesignCode(ldCode);

    if (from.getArchived() != null) to.setArchived(from.getArchived());
    to.setNonStandardAlias(from.getNonStandardAlias());
    if (from.getGroupId() != null) {
      to.setGroupId(from.getGroupId());
    }
    if (from.getGroupDescription() != null) {
      to.setGroupDescription(from.getGroupDescription());
    }
    return to;
  }

  public static PoolOrderDto asDto(PoolOrder from) {
    PoolOrderDto dto = new PoolOrderDto();
    dto.setId(from.getId());
    dto.setPool(asDto(from.getPool(), false));
    dto.setParameters(asDto(from.getSequencingParameter()));
    dto.setPartitions(from.getPartitions());
    dto.setCreatedById(from.getCreatedBy().getUserId());
    dto.setCreationDate(formatDateTime(from.getCreationDate()));
    dto.setUpdatedById(from.getUpdatedBy().getUserId());
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    return dto;
  }

  public static Set<PoolOrderDto> asPoolOrderDtos(Collection<PoolOrder> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toSet());
  }

  public static PoolOrder to(PoolOrderDto from) {
    PoolOrder to = new PoolOrderImpl();
    if (from.getId() != null) to.setId(from.getId());
    to.setPool(to(from.getPool()));
    to.setSequencingParameter(to(from.getParameters()));
    to.setPartitions(from.getPartitions());
    return to;
  }

  public static SequencingParametersDto asDto(SequencingParameters from) {
    SequencingParametersDto dto = new SequencingParametersDto();
    dto.setId(from.getId());
    dto.setName(from.getName());
    dto.setPlatform(asDto(from.getPlatform()));
    return dto;
  }

  public static SequencingParameters to(SequencingParametersDto from) {
    SequencingParameters to = new SequencingParameters();
    to.setId(from.getId());
    to.setName(from.getName());
    if (from.getPlatform() != null) {
      to.setPlatform(to(from.getPlatform()));
    }
    return to;
  }

  public static List<SequencingParametersDto> asSequencingParametersDtos(Collection<SequencingParameters> from) {
    List<SequencingParametersDto> dtoList = from.stream().map(Dtos::asDto).collect(Collectors.toList());
    Collections.sort(dtoList, new Comparator<SequencingParametersDto>() {

      @Override
      public int compare(SequencingParametersDto o1, SequencingParametersDto o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
    return dtoList;
  }

  private static LibraryDto asDto(Library from, boolean includeBoxables) {
    LibraryDto dto = null;
    if (isDetailedLibrary(from)) {
      dto = asDetailedLibraryDto((DetailedLibrary) from);
    } else {
      dto = new LibraryDto();
    }
    dto.setAlias(from.getAlias());
    dto.setName(from.getName());
    dto.setParentSampleId(from.getSample().getId());
    dto.setParentSampleAlias(from.getSample().getAlias());
    if (from.getSample() instanceof DetailedSample) {
      dto.setParentSampleClassId(((DetailedSample) from.getSample()).getSampleClass().getId());
    }
    dto.setCreationDate(formatDate(from.getCreationDate()));
    dto.setDescription(from.getDescription());
    dto.setId(from.getId());
    if (from.getInitialConcentration() != null) {
      dto.setConcentration(from.getInitialConcentration().toString());
    }
    if (from.getLibrarySelectionType() != null) {
      dto.setLibrarySelectionTypeId(from.getLibrarySelectionType().getId());
    }
    if (from.getLibraryStrategyType() != null) {
      dto.setLibraryStrategyTypeId(from.getLibraryStrategyType().getId());
    }
    if (from.getLibraryType() != null) {
      dto.setLibraryTypeId(from.getLibraryType().getId());
      dto.setLibraryTypeAlias(from.getLibraryType().getDescription());
    }
    dto.setQcPassed(from.getQcPassed());
    dto.setLowQuality(from.isLowQuality());
    dto.setPaired(from.getPaired());
    if (from.getPlatformType() != null) {
      dto.setPlatformType(from.getPlatformType().getKey());
    }
    dto.setLastModified(formatDateTime(from.getLastModified()));
    if (from.getKitDescriptor() != null) {
      dto.setKitDescriptorId(from.getKitDescriptor().getId());
    }
    if (!from.getIndices().isEmpty()) {
      dto.setIndexFamilyName(from.getIndices().get(0).getFamily().getName());
      for (Index index : from.getIndices()) {
        switch (index.getPosition()) {
        case 1:
          dto.setIndex1Id(index.getId());
          dto.setIndex1Label(index.getLabel());
          break;
        case 2:
          dto.setIndex2Id(index.getId());
          dto.setIndex2Label(index.getLabel());
          break;
        default:
          throw new IllegalArgumentException("Index at position " + index.getPosition());
        }
      }
    }
    if (from.getVolume() != null) {
      dto.setVolume(from.getVolume().toString());
    }
    dto.setDnaSize(from.getDnaSize());
    dto.setIdentificationBarcode(from.getIdentificationBarcode());
    if (from.getQCs() != null && !from.getQCs().isEmpty()) {
      dto.setQcs(asQcDtos(from.getQCs()));
    }
    dto.setLocationBarcode(from.getLocationBarcode());
    dto.setLocationLabel(BoxUtils.makeLocationLabel(from));
    if (from.getBox() != null) {
      dto.setBox(asDto(from.getBox(), includeBoxables));
      dto.setBoxPosition(from.getBoxPosition());
    }
    dto.setDiscarded(from.isDiscarded());
    if (from.getSample().getBox() != null) {
      dto.setSampleBoxPositionLabel(BoxUtils.makeBoxPositionLabel(from.getSample().getBox().getAlias(), from.getSample().getBoxPosition()));
    }
    if (from.getReceivedDate() != null) {
      dto.setReceivedDate(formatDate(from.getReceivedDate()));
    }
    return dto;
  }

  public static LibraryDto asDto(Library from) {
    return asDto(from, true);
  }

  public static LibraryDto asMinimalDto(Library from) {
    return asDto(from, false);
  }

  public static List<LibraryDto> asLibraryDtos(Collection<Library> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toList());
  }

  public static Library to(LibraryDto from) {
    Library to = null;
    if (from instanceof DetailedLibraryDto) {
      to = toDetailedLibrary((DetailedLibraryDto) from);
    } else {
      to = new LibraryImpl();
    }
    if (from.getId() != null) to.setId(from.getId());

    to.setAlias(from.getAlias());
    to.setName(from.getName());
    to.setDescription(from.getDescription());
    to.setIdentificationBarcode(from.getIdentificationBarcode());
    to.setInitialConcentration(from.getConcentration() == null ? null : Double.valueOf(from.getConcentration()));
    to.setLowQuality(from.getLowQuality());
    if (from.getPaired() != null) {
      to.setPaired(from.getPaired());
    }
    to.setPlatformType(PlatformType.get(from.getPlatformType()));
    if (from.getParentSampleId() != null) {
      to.setSample(new SampleImpl());
      to.getSample().setId(from.getParentSampleId());
    }
    if (from.getLibrarySelectionTypeId() != null) {
      LibrarySelectionType sel = new LibrarySelectionType();
      sel.setId(from.getLibrarySelectionTypeId());
      to.setLibrarySelectionType(sel);
    }
    if (from.getLibraryStrategyTypeId() != null) {
      LibraryStrategyType strat = new LibraryStrategyType();
      strat.setId(from.getLibraryStrategyTypeId());
      to.setLibraryStrategyType(strat);
    }
    if (from.getLibraryTypeId() != null) {
      LibraryType type = new LibraryType();
      type.setId(from.getLibraryTypeId());
      if (from.getLibraryTypeAlias() != null) type.setDescription(from.getLibraryTypeAlias());
      to.setLibraryType(type);
    }
    to.setQcPassed(from.getQcPassed());
    if (from.getIndex1Id() != null) {
      List<Index> indices = new ArrayList<>();
      Index tb1 = new Index();
      tb1.setId(from.getIndex1Id());
      indices.add(tb1);
      if (from.getIndex2Id() != null) {
        Index tb2 = new Index();
        tb2.setId(from.getIndex2Id());
        indices.add(tb2);
      }
      to.setIndices(indices);
    }
    if (from.getVolume() != null) {
      to.setVolume(Double.valueOf(from.getVolume()));
    }
    to.setDnaSize(from.getDnaSize());
    if (from.getKitDescriptorId() != null) {
      KitDescriptor kitDescriptor = new KitDescriptor();
      kitDescriptor.setId(from.getKitDescriptorId());
      to.setKitDescriptor(kitDescriptor);
    }
    to.setLocationBarcode(from.getLocationBarcode());
    to.setCreationDate(parseDate(from.getCreationDate()));
    if (from.getReceivedDate() != null) {
      to.setReceivedDate(parseDate(from.getReceivedDate()));
    }
    to.setBoxPosition((LibraryBoxPosition) makeBoxablePosition(from, (LibraryImpl) to));
    to.setDiscarded(from.isDiscarded());
    return to;
  }

  public static List<BoxDto> asBoxDtos(Collection<Box> boxes, boolean includeBoxables) {
    return boxes.stream()
        .map(box -> asDto(box, includeBoxables))
        .collect(Collectors.toList());
  }

  public static BoxDto asDto(Box from, boolean includeBoxables) {
    BoxDto dto = new BoxDto();
    dto.setId(from.getId());
    dto.setName(from.getName());
    dto.setAlias(from.getAlias());
    dto.setDescription(from.getDescription());
    dto.setIdentificationBarcode(from.getIdentificationBarcode());
    dto.setLocationBarcode(from.getLocationBarcode());
    if (from.getUse() != null) {
      dto.setUseId(from.getUse().getId());
      dto.setUseAlias(from.getUse().getAlias());
    }
    if (from.getSize() != null) {
      dto.setSizeId(from.getSize().getId());
      dto.setRows(from.getSize().getRows());
      dto.setCols(from.getSize().getColumns());
      dto.setScannable(from.getSize().getScannable());
    }
    if (includeBoxables && from.getBoxables() != null) {
      dto.setItems(asBoxablesDtos(from.getBoxables()));
    }
    if (from.getStorageLocation() != null) {
      dto.setFreezerDisplayLocation(from.getStorageLocation().getFreezerDisplayLocation());
      dto.setStorageDisplayLocation(from.getStorageLocation().getFullDisplayLocation());
    }
    dto.setTubeCount(from.getTubeCount());
    return dto;
  }

  private static List<BoxableDto> asBoxablesDtos(Map<String, BoxableView> boxables) {
    return boxables.entrySet().stream().map(entry -> asDto(entry.getValue())).collect(Collectors.toList());
  }

  public static List<BoxableDto> asBoxableDtos(List<BoxableView> boxables) {
    return boxables.stream()
        .map(Dtos::asDto)
        .collect(Collectors.toList());
  }

  public static BoxableDto asDto(BoxableView from) {
    BoxableDto dto = new BoxableDto();
    dto.setId(from.getId().getTargetId());
    dto.setAlias(from.getAlias());
    dto.setBoxAlias(from.getBoxAlias());
    dto.setBoxPosition(BoxUtils.makeLocationLabel(from));
    dto.setCoordinates(from.getBoxPosition());
    dto.setDiscarded(from.isDiscarded());
    dto.setIdentificationBarcode(from.getIdentificationBarcode());
    dto.setName(from.getName());
    dto.setVolume(from.getVolume());
    dto.setEntityType(from.getId().getTargetType());
    dto.setSampleClassId(from.getSampleClassId());
    return dto;
  }

  public static Box to(BoxDto from) {
    Box to = new BoxImpl();
    if (from.getId() != null) to.setId(from.getId());
    to.setAlias(from.getAlias());
    to.setDescription(from.getDescription());
    to.setIdentificationBarcode(from.getIdentificationBarcode());
    return to;
  }

  private static DilutionDto asDto(LibraryDilution from, LibraryDto libraryDto, boolean includeBoxables) {
    DilutionDto dto = new DilutionDto();
    dto.setId(from.getId());
    dto.setName(from.getName());
    dto.setDilutionUserName(from.getDilutionCreator());
    dto.setConcentration(from.getConcentration() == null ? null : from.getConcentration().toString());
    dto.setVolume(from.getVolume() == null ? null : from.getVolume().toString());
    if (from.getCreationDate() != null) {
      dto.setCreationDate(formatDate(from.getCreationDate()));
    }
    dto.setIdentificationBarcode(from.getIdentificationBarcode());
    dto.setLocationLabel(BoxUtils.makeLocationLabel(from));
    if (from.getTargetedSequencing() != null) {
      dto.setTargetedSequencingId(from.getTargetedSequencing().getId());
    }
    dto.setLibrary(libraryDto);
    if (from.getBox() != null) {
      dto.setBox(asDto(from.getBox(), includeBoxables));
      dto.setBoxPosition(from.getBoxPosition());
    }
    dto.setDiscarded(from.isDiscarded());
    return dto;
  }

  public static DilutionDto asMinimalDto(LibraryDilution from) {
    Library lib = from.getLibrary();
    LibraryDto libDto = new LibraryDto();
    libDto.setId(lib.getId());
    libDto.setName(lib.getName());
    libDto.setAlias(lib.getAlias());
    libDto.setIdentificationBarcode(lib.getIdentificationBarcode());
    if (lib.getPlatformType() != null) {
      libDto.setPlatformType(lib.getPlatformType().getKey());
    }

    return asDto(from, libDto, false);
  }

  public static DilutionDto asDto(LibraryDilution from) {
    return asDto(from, asDto(from.getLibrary()), true);

  }

  public static DilutionDto asDto(PoolableElementView from) {
    DilutionDto dto = new DilutionDto();
    dto.setId(from.getDilutionId());
    dto.setName(from.getDilutionName());
    dto.setDilutionUserName(from.getCreatorName());
    dto.setConcentration(from.getDilutionConcentration() == null ? null : from.getDilutionConcentration().toString());
    dto.setLastModified(formatDateTime(from.getLastModified()));
    dto.setCreationDate(formatDate(from.getCreated()));
    dto.setIdentificationBarcode(from.getDilutionBarcode());
    dto.setIndexIds(from.getIndices().stream().map(Index::getId).collect(Collectors.toList()));
    dto.setTargetedSequencingId(from.getTargetedSequencingId());
    dto.setVolume(from.getDilutionVolume() == null ? null : from.getDilutionVolume().toString());

    LibraryDto ldto = new LibraryDto();
    ldto.setId(from.getLibraryId());
    ldto.setName(from.getLibraryName());
    ldto.setAlias(from.getLibraryAlias());
    ldto.setIdentificationBarcode(from.getLibraryBarcode());
    ldto.setLowQuality(from.isLowQualityLibrary());
    ldto.setParentSampleId(from.getSampleId());
    ldto.setParentSampleAlias(from.getSampleAlias());
    if (from.getPlatformType() != null) {
      ldto.setPlatformType(from.getPlatformType().getKey());
    }
    dto.setLibrary(ldto);

    Sample sample = from.getSample();
    if (isDetailedSample(sample)) {
      dto.setIdentityConsentLevel(getIdentityConsentLevelString((DetailedSample) sample));
    }
    return dto;
  }

  public static LibraryDilution to(DilutionDto from) {
    LibraryDilution to = new LibraryDilution();
    if (from.getId() != null) to.setId(from.getId());
    if (!isStringEmptyOrNull(from.getName())) {
      to.setName(from.getName());
    }
    to.setIdentificationBarcode(from.getIdentificationBarcode());
    to.setConcentration(from.getConcentration() == null ? null : Double.valueOf(from.getConcentration()));
    to.setVolume(from.getVolume() == null ? null : Double.valueOf(from.getVolume()));
    to.setLibrary(to(from.getLibrary()));
    to.setDilutionCreator(from.getDilutionUserName());
    to.setCreationDate(parseDate(from.getCreationDate()));
    if (from.getTargetedSequencingId() != null) {
      to.setTargetedSequencing(new TargetedSequencing());
      to.getTargetedSequencing().setId(from.getTargetedSequencingId());
    }
    to.setBoxPosition((DilutionBoxPosition) makeBoxablePosition(from, to));
    to.setDiscarded(from.isDiscarded());
    return to;
  }

  public static PoolDto asDto(Pool from, boolean includeContents) {
    PoolDto dto = new PoolDto();
    dto.setId(from.getId());
    dto.setName(from.getName());
    dto.setAlias(from.getAlias());
    dto.setDescription(from.getDescription());
    dto.setConcentration(from.getConcentration() == null ? null : from.getConcentration().toString());
    dto.setQcPassed(from.getQcPassed());
    dto.setCreationDate(formatDate(from.getCreationDate()));
    if (from.getVolume() != null) {
      dto.setVolume(from.getVolume().toString());
    }
    dto.setPlatformType(from.getPlatformType().name());
    dto.setLongestIndex(from.getLongestIndex());
    dto.setLastModified(formatDateTime(from.getLastModified()));
    dto.setDilutionCount(from.getPoolableElementViews().size());
    from.getPoolableElementViews().stream()//
        .map(PoolableElementView::getLibraryDnaSize)//
        .filter(Objects::nonNull)//
        .mapToDouble(Long::doubleValue)//
        .average()//
        .ifPresent(dto::setInsertSize);
    if (includeContents) {
      Set<DilutionDto> pooledElements = new HashSet<>();
      for (PoolableElementView ld : from.getPoolableElementViews()) {
        if (ld != null) {
          pooledElements.add(asDto(ld));
        }
      }
      dto.setPooledElements(pooledElements);
      dto.setDuplicateIndicesSequences(from.getDuplicateIndicesSequences());
      dto.setDuplicateIndices(!dto.getDuplicateIndicesSequences().isEmpty());
      dto.setNearDuplicateIndicesSequences(from.getNearDuplicateIndicesSequences());
      dto.setNearDuplicateIndices(!dto.getNearDuplicateIndicesSequences().isEmpty());
    } else {
      dto.setPooledElements(Collections.emptySet());
      dto.setDuplicateIndices(!from.getDuplicateIndicesSequences().isEmpty());
      dto.setNearDuplicateIndices(!from.getNearDuplicateIndicesSequences().isEmpty());
    }
    dto.setIdentificationBarcode(from.getIdentificationBarcode());
    dto.setLocationLabel(BoxUtils.makeLocationLabel(from));
    if (from.getBox() != null) {
      dto.setBox(asDto(from.getBox(), true));
      dto.setBoxPosition(from.getBoxPosition());
    }
    dto.setDiscarded(from.isDiscarded());
    dto.setHasLowQualityLibraries(from.getHasLowQualityMembers());
    return dto;
  }

  public static List<PoolDto> asPoolDtos(Collection<Pool> poolSubset, boolean includeContents) {
    return poolSubset.stream().map(pool -> asDto(pool, includeContents)).collect(Collectors.toList());
  }

  public static RunDto asDto(Run from) {
    RunDto dto = new RunDto();
    dto.setId(from.getId());
    dto.setName(from.getName());
    dto.setAlias(from.getAlias());
    if (from.getHealth() != null) {
      dto.setStatus(from.getHealth().getKey());
    } else {
      dto.setStatus("");
    }
    dto.setLastModified(formatDateTime(from.getLastModified()));
    if (from.getSequencer() != null) {
      dto.setPlatformType(from.getSequencer().getPlatform().getPlatformType().getKey());
    } else {
      dto.setPlatformType("");
    }
    if (from.getStartDate() != null) {
      dto.setStartDate(formatDate(from.getStartDate()));
    } else {
      dto.setStartDate("");
    }
    if (from.getCompletionDate() != null) {
      dto.setEndDate(formatDate(from.getCompletionDate()));
    }
    if (from.getSequencingParameters() != null) {
      dto.setParameters(asDto(from.getSequencingParameters()));
    } else {
      SequencingParametersDto parametersDto = new SequencingParametersDto();
      parametersDto.setId(-1L);
      parametersDto.setName("(None)");
      dto.setParameters(parametersDto);
    }
    dto.setProgress(from.getProgress());
    return dto;
  }

  public static List<RunDto> asRunDtos(Collection<Run> runSubset) {
    return runSubset.stream().map(Dtos::asDto).collect(Collectors.toList());
  }

  public static ContainerDto asDto(SequencerPartitionContainer from) {
    ContainerDto dto = new ContainerDto();
    dto.setId(from.getId());
    dto.setIdentificationBarcode(from.getIdentificationBarcode());
    dto.setModel(asDto(from.getModel()));
    Run lastRun = from.getLastRun();
    if (lastRun != null) {
      dto.setLastRunAlias(lastRun.getAlias());
      dto.setLastRunId(lastRun.getId());
      dto.setLastSequencerId(lastRun.getSequencer().getId());
      dto.setLastSequencerName(lastRun.getSequencer().getName());
    }
    if (from.getLastModified() != null) {
      dto.setLastModified(formatDateTime(from.getLastModified()));
    }
    if (from.getClusteringKit() != null) {
      dto.setClusteringKit(asDto(from.getClusteringKit()));
    }
    if (from.getMultiplexingKit() != null) {
      dto.setMultiplexingKit(asDto(from.getMultiplexingKit()));
    }
    return dto;
  }

  public static List<ContainerDto> asContainerDtos(Collection<SequencerPartitionContainer> containerSubset) {
    return containerSubset.stream().map(Dtos::asDto).collect(Collectors.toList());
  }

  public static ContainerModelDto asDto(SequencingContainerModel from) {
    ContainerModelDto dto = new ContainerModelDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    dto.setIdentificationBarcode(from.getIdentificationBarcode());
    dto.setPlatformType(from.getPlatformType().name());
    dto.setPlatformIds(from.getPlatforms().stream().map(Platform::getId).collect(Collectors.toList()));
    dto.setPartitionCount(from.getPartitionCount());
    dto.setArchived(from.isArchived());
    return dto;
  }

  public static List<ContainerModelDto> asDtos(Collection<SequencingContainerModel> models) {
    return models.stream().map(Dtos::asDto).collect(Collectors.toList());
  }

  public static QcTypeDto asDto(QcType from) {
    QcTypeDto dto = new QcTypeDto();
    dto.setId(from.getQcTypeId());
    dto.setName(from.getName());
    dto.setDescription(from.getDescription());
    dto.setQcTarget(from.getQcTarget());
    dto.setUnits(from.getUnits());
    dto.setPrecisionAfterDecimal(from.getPrecisionAfterDecimal());
    dto.setArchived(from.isArchived());
    return dto;
  }

  public static QcType to(QcTypeDto from) {
    QcType to = new QcType();
    if (from.getId() != null) to.setQcTypeId(from.getId());
    to.setName(from.getName());
    to.setDescription(from.getDescription());
    to.setQcTarget(from.getQcTarget());
    to.setUnits(from.getUnits());
    to.setPrecisionAfterDecimal(from.getPrecisionAfterDecimal());
    to.setArchived(from.isArchived());
    return to;
  }

  public static QcDto asDto(QC from) {
    QcDto dto = new QcDto();
    dto.setId(from.getId());
    dto.setDate(formatDate(from.getDate()));
    dto.setCreator(from.getCreator().getFullName());
    dto.setType(asDto(from.getType()));
    dto.setResults(from.getResults());
    dto.setEntityId(from.getEntity().getId());
    dto.setEntityAlias(from.getEntity().getAlias());
    return dto;
  }

  public static List<QcDto> asQcDtos(Collection<? extends QC> qcSubset) {
    return qcSubset.stream().map(Dtos::asDto).collect(Collectors.toList());
  }

  public static List<QcTypeDto> asQcTypeDtos(Collection<QcType> qcTypeSubset) {
    return qcTypeSubset.stream().map(Dtos::asDto).collect(Collectors.toList());
  }

  public static PoolOrderCompletionDto asDto(PoolOrderCompletion from) {
    PoolOrderCompletionDto dto = new PoolOrderCompletionDto();
    dto.setId(from.getPool().getId() + "_" + from.getSequencingParameters().getId());
    dto.setPool(asDto(from.getPool(), false));
    dto.setParameters(asDto(from.getSequencingParameters()));
    dto.setLastUpdated(formatDateTime(from.getLastUpdated()));
    dto.setRemaining(from.getRemaining());
    dto.setCompleted(from.get(HealthType.Completed));
    dto.setFailed(from.get(HealthType.Failed));
    dto.setRequested(from.get(HealthType.Requested));
    dto.setRunning(from.get(HealthType.Running));
    dto.setStarted(from.get(HealthType.Started));
    dto.setStopped(from.get(HealthType.Stopped));
    dto.setUnknown(from.get(HealthType.Unknown));
    dto.setLoaded(from.getLoaded());
    return dto;
  }

  public static PlatformDto asDto(Platform from) {
    PlatformDto dto = new PlatformDto();
    dto.setId(from.getId());
    dto.setPlatformType(from.getPlatformType().name());
    dto.setDescription(from.getDescription());
    dto.setInstrumentModel(from.getInstrumentModel());
    dto.setNumContainers(from.getNumContainers());
    dto.setInstrumentType(from.getInstrumentType().name());
    return dto;
  }

  public static Platform to(PlatformDto from) {
    Platform to = new Platform();
    to.setId(from.getId());
    to.setPlatformType(PlatformType.get(from.getPlatformType()));
    to.setDescription(from.getDescription());
    to.setInstrumentModel(from.getInstrumentModel());
    to.setNumContainers(from.getNumContainers());
    to.setInstrumentType(InstrumentType.valueOf(from.getInstrumentType()));
    return to;
  }

  public static ProjectDto asDto(Project from) {
    ProjectDto dto = new ProjectDto();
    dto.setId(from.getId());
    dto.setName(from.getName());
    dto.setAlias(from.getAlias());
    dto.setShortName(from.getShortName());
    dto.setDescription(from.getDescription());
    dto.setProgress(from.getProgress().getKey());
    return dto;
  }

  public static List<ProjectDto> asProjectDtos(Collection<Project> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toList());
  }

  public static LibraryDesignDto asDto(LibraryDesign from) {
    LibraryDesignDto dto = new LibraryDesignDto();
    dto.setId(from.getId());
    dto.setName(from.getName());
    dto.setDesignCodeId(from.getLibraryDesignCode().getId());
    dto.setSampleClassId(from.getSampleClass().getId());
    dto.setSelectionId(from.getLibrarySelectionType().getId());
    dto.setStrategyId(from.getLibraryStrategyType().getId());
    return dto;
  }

  public static LibraryTypeDto asDto(LibraryType from) {
    LibraryTypeDto dto = new LibraryTypeDto();
    dto.setId(from.getId());
    dto.setAlias(from.getDescription());
    dto.setArchived(from.getArchived());
    dto.setPlatform(from.getPlatformType().name());
    return dto;
  }

  public static LibrarySelectionTypeDto asDto(LibrarySelectionType from) {
    LibrarySelectionTypeDto dto = new LibrarySelectionTypeDto();
    dto.setId(from.getId());
    dto.setAlias(from.getDescription());
    dto.setName(from.getName());
    return dto;
  }

  public static LibraryStrategyTypeDto asDto(LibraryStrategyType from) {
    LibraryStrategyTypeDto dto = new LibraryStrategyTypeDto();
    dto.setId(from.getId());
    dto.setDescription(from.getDescription());
    dto.setName(from.getName());
    return dto;
  }

  public static IndexDto asDto(Index from) {
    return asDto(from, true);
  }

  public static IndexDto asDto(Index from, boolean includeFamily) {
    IndexDto dto = new IndexDto();
    dto.setId(from.getId());
    dto.setLabel(from.getLabel());
    dto.setName(from.getName());
    dto.setPosition(from.getPosition());
    dto.setSequence(from.getSequence());
    if (includeFamily) {
      dto.setFamily(asDto(from.getFamily(), false));
    }
    return dto;
  }

  public static IndexFamilyDto asDto(IndexFamily from) {
    return asDto(from, true);
  }

  private static IndexFamilyDto asDto(IndexFamily from, boolean includeChildren) {
    IndexFamilyDto dto = new IndexFamilyDto();
    dto.setId(from.getId());
    dto.setName(from.getName());
    dto.setArchived(from.getArchived());
    if (includeChildren) {
      dto.setIndices(from.getIndices().stream().map(x -> asDto(x, false)).collect(Collectors.toList()));
    }
    dto.setMaximumNumber(from.getMaximumNumber());
    dto.setPlatformType(from.getPlatformType() == null ? null : from.getPlatformType().name());
    dto.setFakeSequence(from.hasFakeSequence());
    return dto;
  }

  public static StainDto asDto(Stain from) {
    StainDto dto = new StainDto();
    dto.setId(from.getId());
    dto.setCategory(from.getCategory() == null ? null : from.getCategory().getName());
    dto.setName(from.getName());
    return dto;
  }

  public static Run to(NotificationDto from, User user) {
    final Run to = from.getPlatformType().createRun(user);
    setCommonRunValues(from, to);

    switch (to.getPlatformType()) {
    case PACBIO:
      break;
    case ILLUMINA:
      setIlluminaRunValues((IlluminaNotificationDto) from, (IlluminaRun) to);
      break;
    case LS454:
      to.setPairedEnd(((LS454NotificationDto) from).isPairedEndRun());
      ((LS454Run) to).setCycles(((LS454NotificationDto) from).getCycles());
      break;
    case SOLID:
      to.setPairedEnd(((SolidNotificationDto) from).isPairedEndRun());
      break;
    default:
      throw new NotImplementedException();
    }
    return to;
  }

  private static void setIlluminaRunValues(IlluminaNotificationDto from, IlluminaRun to) {
    to.setPairedEnd(from.isPairedEndRun());
    to.setNumCycles(from.getNumCycles());
    to.setImgCycle(from.getImgCycle());
    to.setCallCycle(from.getCallCycle());
    to.setScoreCycle(from.getScoreCycle());
    to.setRunBasesMask(from.getRunBasesMask());
  }

  private static void setCommonRunValues(NotificationDto from, Run to) {
    to.setAlias(from.getRunAlias());
    to.setFilePath(from.getSequencerFolderPath());
    to.setHealth(from.getHealthType());
    to.setStartDate(LimsUtils.toBadDate(from.getStartDate()));
    to.setCompletionDate(LimsUtils.toBadDate(from.getCompletionDate()));
    to.setMetrics(from.getMetrics());
  }

  public static TargetedSequencingDto asDto(TargetedSequencing from) {
    TargetedSequencingDto dto = new TargetedSequencingDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    dto.setKitDescriptorIds(from.getKitDescriptors().stream().map(KitDescriptor::getId).collect(Collectors.toList()));
    return dto;
  }

  public static Pool to(PoolDto dto) {
    PoolImpl to = new PoolImpl();
    to.setId(dto.getId() == null ? PoolImpl.UNSAVED_ID : dto.getId());
    to.setAlias(dto.getAlias());
    to.setConcentration(dto.getConcentration() == null ? null : Double.valueOf(dto.getConcentration()));
    to.setCreationDate(parseDate(dto.getCreationDate()));
    to.setDescription(dto.getDescription());
    to.setIdentificationBarcode(dto.getIdentificationBarcode());
    to.setDiscarded(dto.isDiscarded());
    if (dto.getVolume() != null) {
      to.setVolume(Double.valueOf(dto.getVolume()));
    }
    to.setPlatformType(PlatformType.valueOf(dto.getPlatformType()));
    to.setPoolableElementViews(dto.getPooledElements().stream().map(dilution -> {
      PoolableElementView view = new PoolableElementView();
      view.setDilutionId(dilution.getId());
      view.setDilutionName(dilution.getName());
      return view;
    }).collect(Collectors.toSet()));
    to.setQcPassed(dto.getQcPassed());
    to.setBoxPosition((PoolBoxPosition) makeBoxablePosition(dto, to));
    return to;
  }

  public static PrinterBackendDto asDto(Backend from) {
    PrinterBackendDto dto = new PrinterBackendDto();
    dto.setId(from.ordinal());
    dto.setName(from.name());
    dto.setConfigurationKeys(from.getConfigurationKeys());
    return dto;
  }

  public static PrinterDriverDto asDto(Driver from) {
    PrinterDriverDto dto = new PrinterDriverDto();
    dto.setId(from.ordinal());
    dto.setName(from.name());
    return dto;
  }

  public static PrinterDto asDto(Printer from) {
    PrinterDto dto = new PrinterDto();
    dto.setId(from.getId());
    dto.setAvailable(from.isEnabled());
    dto.setBackend(from.getBackend().name());
    // We intentionally do not pass configuration to the front end since it has passwords in it.
    dto.setDriver(from.getDriver().name());
    dto.setName(from.getName());
    return dto;
  }

  public static Printer to(PrinterDto dto) throws JsonProcessingException {
    Printer to = new Printer();
    to.setId(dto.getId());
    to.setBackend(Backend.valueOf(dto.getBackend()));
    to.setConfiguration(new ObjectMapper().writeValueAsString(dto.getConfiguration()));
    to.setDriver(Driver.valueOf(dto.getDriver()));
    to.setEnabled(dto.isAvailable());
    to.setName(dto.getName());

    return to;
  }

  public static StudyDto asDto(Study from) {
    StudyDto dto = new StudyDto();
    dto.setId(from.getId());
    dto.setAccession(from.getAccession());
    dto.setAlias(from.getAlias());
    dto.setDescription(from.getDescription());
    dto.setName(from.getName());
    dto.setProjectId(from.getProject().getId());
    dto.setStudyTypeId(from.getStudyType().getId());
    return dto;
  }

  public static StudyTypeDto asDto(StudyType from) {
    StudyTypeDto dto = new StudyTypeDto();
    dto.setId(from.getId());
    dto.setName(from.getName());
    return dto;
  }

  public static ChangeLogDto asDto(ChangeLog from) {
    ChangeLogDto dto = new ChangeLogDto();
    dto.setSummary(from.getSummary());
    dto.setTime(formatDateTime(from.getTime()));
    dto.setUserName(from.getUser().getFullName());
    return dto;
  }

  public static UserDto asDto(User from) {
    UserDto dto = new UserDto();
    dto.setId(from.getUserId());
    dto.setActive(from.isActive());
    dto.setAdmin(from.isAdmin());
    dto.setEmail(from.getEmail());
    dto.setExternal(from.isExternal());
    dto.setFullName(from.getFullName());
    dto.setInternal(from.isInternal());
    dto.setLoginName(from.getLoginName());
    return dto;
  }

  public static GroupDto asDto(Group from) {
    GroupDto dto = new GroupDto();
    dto.setId(from.getGroupId());
    dto.setDescription(from.getDescription());
    dto.setName(from.getName());
    return dto;
  }

  public static InstrumentDto asDto(Instrument from) {
    InstrumentDto dto = new InstrumentDto();
    dto.setId(from.getId());
    dto.setDateCommissioned(formatDate(from.getDateCommissioned()));
    dto.setDateDecommissioned(formatDate(from.getDateDecommissioned()));
    dto.setIp(from.getIpAddress());
    dto.setName(from.getName());
    dto.setPlatform(asDto(from.getPlatform()));
    dto.setSerialNumber(from.getSerialNumber());
    return dto;
  }

  public static Instrument to(InstrumentDto dto) {
    Instrument to = new InstrumentImpl();
    to.setId(dto.getId());
    to.setDateCommissioned(parseDate(dto.getDateCommissioned()));
    to.setDateDecommissioned(parseDate(dto.getDateDecommissioned()));
    to.setIpAddress(dto.getIp());
    to.setName(dto.getName());
    to.setPlatform(to(dto.getPlatform()));
    to.setSerialNumber(dto.getSerialNumber());
    return to;
  }

  public static QC to(QcDto dto) {
    QC to;
    switch (dto.getType().getQcTarget()) {
    case Library:
      LibraryQC newLibraryQc = new LibraryQC();
      Library ownerLibrary = new LibraryImpl();
      ownerLibrary.setId(dto.getEntityId());
      newLibraryQc.setLibrary(ownerLibrary);
      to = newLibraryQc;
      break;
    case Sample:
      SampleQC newSampleQc = new SampleQC();
      Sample ownerSample = new SampleImpl();
      ownerSample.setId(dto.getEntityId());
      newSampleQc.setSample(ownerSample);
      to = newSampleQc;
      break;
    case Pool:
      PoolQC newPoolQc = new PoolQC();
      Pool ownerPool = new PoolImpl();
      ownerPool.setId(dto.getEntityId());
      newPoolQc.setPool(ownerPool);
      to = newPoolQc;
      break;
    case Container:
      ContainerQC newContainerQc = new ContainerQC();
      SequencerPartitionContainer ownerContainer = new SequencerPartitionContainerImpl();
      ownerContainer.setId(dto.getEntityId());
      newContainerQc.setContainer(ownerContainer);
      to = newContainerQc;
      break;
    default:
      throw new IllegalArgumentException("No such QC target: " + dto.getType().getQcTarget());
    }
    if (dto.getId() != null) {
      to.setId(dto.getId());
    }
    to.setDate(parseDate(dto.getDate()));
    to.setResults(dto.getResults());
    to.setType(to(dto.getType()));
    return to;
  }

  public static ReferenceGenomeDto asDto(ReferenceGenome from) {
    ReferenceGenomeDto dto = new ReferenceGenomeDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    return dto;
  }

  public static PartitionDto asDto(Partition from) {
    PartitionDto dto = new PartitionDto();
    dto.setId(from.getId());
    dto.setContainerId(from.getSequencerPartitionContainer().getId());
    dto.setContainerName(from.getSequencerPartitionContainer().getIdentificationBarcode());
    dto.setPartitionNumber(from.getPartitionNumber());
    dto.setPool(from.getPool() == null ? null : asDto(from.getPool(), false));
    return dto;
  }

  public static PartitionQCTypeDto asDto(PartitionQCType from) {
    PartitionQCTypeDto dto = new PartitionQCTypeDto();
    dto.setId(from.getId());
    dto.setDescription(from.getDescription());
    dto.setNoteRequired(from.isNoteRequired());
    return dto;
  }

  public static ExperimentDto asDto(Experiment from) {
    ExperimentDto dto = new ExperimentDto();
    dto.setId(from.getId());
    dto.setAccession(from.getAccession());
    dto.setAlias(from.getAlias());
    dto.setDescription(from.getDescription());
    dto.setName(from.getName());
    dto.setPlatform(asDto(from.getPlatform()));
    dto.setLibrary(asDto(from.getLibrary()));
    dto.setPartitions(from.getRunPartitions().stream()
        .map(entry -> new ExperimentDto.RunPartitionDto(asDto(entry.getRun()), asDto(entry.getPartition()))).collect(Collectors.toList()));
    dto.setStudy(asDto(from.getStudy()));
    dto.setTitle(from.getTitle());
    return dto;
  }

  public static KitConsumableDto asDto(Kit from) {
    KitConsumableDto dto = new KitConsumableDto();
    dto.setId(from.getId());
    dto.setDate(formatDate(from.getKitDate()));
    dto.setDescriptor(asDto(from.getKitDescriptor()));
    dto.setLotNumber(from.getLotNumber());
    return dto;
  }

  public static Kit to(KitConsumableDto dto) {
    Kit to = new KitImpl();
    if (dto.getId() != null) {
      to.setId(dto.getId());
    }
    to.setKitDate(parseDate(dto.getDate()));
    to.setKitDescriptor(to(dto.getDescriptor()));
    to.setLotNumber(dto.getLotNumber());
    return to;
  }

  public static Experiment to(ExperimentDto dto) {
    Experiment to = new Experiment();
    to.setId(dto.getId());
    to.setAlias(dto.getAlias());
    to.setDescription(dto.getDescription());
    to.setLibrary(to(dto.getLibrary()));
    to.setPlatform(to(dto.getPlatform()));
    to.setRunPartitions(dto.getPartitions().stream().map(rpDto -> {
      RunPartition rpTo = new RunPartition();
      rpTo.setExperiment(to);
      rpTo.setPartition(to(rpDto.getPartition()));
      rpTo.setRun(to(rpDto.getRun()));
      return rpTo;
    }).collect(Collectors.toList()));
    to.setStudy(to(dto.getStudy()));
    to.setTitle(dto.getTitle());
    return to;
  }

  public static Study to(StudyDto dto) {
    Study to = new StudyImpl();
    to.setId(dto.getId());
    return to;
  }

  public static Run to(RunDto dto) {
    Run to = PlatformType.get(dto.getPlatformType()).createRun(null);
    to.setId(dto.getId());
    return to;
  }

  public static Partition to(PartitionDto dto) {
    Partition to = new PartitionImpl();
    to.setId(dto.getId());
    return to;
  }

  public static SubmissionDto asDto(Submission from) {
    SubmissionDto dto = new SubmissionDto();
    dto.setId(from.getId());
    dto.setAccession(from.getAccession());
    dto.setAlias(from.getAlias());
    dto.setCompleted(from.isCompleted());
    dto.setCreationDate(formatDate(from.getCreationDate()));
    dto.setDescription(from.getDescription());
    dto.setSubmittedDate(formatDate(from.getSubmissionDate()));
    dto.setTitle(from.getTitle());
    dto.setVerified(from.isVerified());
    return dto;
  }

  public static ArrayDto asDto(Array from) {
    ArrayDto dto = new ArrayDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    if (from.getArrayModel() != null) {
      dto.setArrayModelId(from.getArrayModel().getId());
      dto.setArrayModelAlias(from.getArrayModel().getAlias());
      dto.setRows(from.getArrayModel().getRows());
      dto.setColumns(from.getArrayModel().getColumns());
    }
    dto.setSerialNumber(from.getSerialNumber());
    dto.setDescription(from.getDescription());
    if (from.getSamples() != null) {
      dto.setSamples(asArraySampleDtos(from.getSamples()));
    }
    if (from.getLastModified() != null) {
      dto.setLastModified(formatDate(from.getLastModified()));
    }
    return dto;
  }

  private static List<ArraySampleDto> asArraySampleDtos(Map<String, Sample> arraySamples) {
    return arraySamples.entrySet().stream().map(entry -> asArraySampleDto(entry.getKey(), entry.getValue())).collect(Collectors.toList());
  }

  private static ArraySampleDto asArraySampleDto(String position, Sample sample) {
    ArraySampleDto dto = new ArraySampleDto();
    dto.setCoordinates(position);
    dto.setId(sample.getId());
    dto.setAlias(sample.getAlias());
    dto.setName(sample.getName());
    dto.setIdentificationBarcode(sample.getIdentificationBarcode());
    return dto;
  }

  public static Array to(ArrayDto from) {
    Array array = new Array();
    if (from.getId() != null) {
      array.setId(from.getId());
    }
    array.setAlias(from.getAlias());
    array.setArrayModel(new ArrayModel());
    if (from.getArrayModelId() != null) {
      array.getArrayModel().setId(from.getArrayModelId());
    }
    if (from.getArrayModelAlias() != null) {
      array.getArrayModel().setAlias(from.getArrayModelAlias());
    }
    array.setSerialNumber(from.getSerialNumber());
    array.setDescription(nullifyStringIfBlank(from.getDescription()));
    array.setSamples(toArraySamples(from.getSamples()));
    return array;
  }

  private static Map<String, Sample> toArraySamples(List<ArraySampleDto> dtos) {
    Map<String, Sample> samples = new HashMap<>();
    if (dtos != null) {
      for (ArraySampleDto dto : dtos) {
        Sample sample = new SampleImpl();
        sample.setId(dto.getId());
        sample.setAlias(dto.getAlias());
        sample.setName(dto.getName());
        sample.setIdentificationBarcode(dto.getIdentificationBarcode());
        samples.put(dto.getCoordinates(), sample);
      }
    }
    return samples;
  }

  public static ArrayRunDto asDto(ArrayRun from) {
    ArrayRunDto dto = new ArrayRunDto();
    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    dto.setDescription(from.getDescription());
    dto.setFilePath(from.getFilePath());
    if (from.getInstrument() != null) {
      dto.setInstrumentId(from.getInstrument().getId());
      dto.setInstrumentName(from.getInstrument().getName());
    }
    if (from.getArray() != null) {
      dto.setArray(asDto(from.getArray()));
    }
    dto.setStatus(from.getHealth().getKey());
    if (from.getStartDate() != null) {
      dto.setStartDate(formatDate(from.getStartDate()));
    }
    if (from.getCompletionDate() != null) {
      dto.setCompletionDate(formatDate(from.getCompletionDate()));
    }
    if (from.getLastModified() != null) {
      dto.setLastModified(formatDate(from.getLastModified()));
    }
    return dto;
  }

  public static List<ArrayDto> asArrayDtos(Collection<Array> arrays) {
    return arrays.stream()
        .map(Dtos::asDto)
        .collect(Collectors.toList());
  }

  public static final ArrayRun to(ArrayRunDto from) {
    ArrayRun run = new ArrayRun();
    if (from.getId() != null) {
      run.setId(from.getId());
    }
    run.setAlias(from.getAlias());
    run.setDescription(nullifyStringIfBlank(from.getDescription()));
    run.setFilePath(nullifyStringIfBlank(from.getFilePath()));
    run.setInstrument(new InstrumentImpl());
    if (from.getInstrumentId() != null) {
      run.getInstrument().setId(from.getInstrumentId());
    }
    if (from.getInstrumentName() != null) {
      run.getInstrument().setName(from.getInstrumentName());
    }
    if (from.getArray() != null) {
      run.setArray(to(from.getArray()));
    }
    run.setHealth(HealthType.get(from.getStatus()));
    if (from.getStartDate() != null) {
      run.setStartDate(parseDate(from.getStartDate()));
    }
    if (from.getCompletionDate() != null) {
      run.setCompletionDate(parseDate(from.getCompletionDate()));
    }
    if (from.getLastModified() != null) {
      run.setLastModified(parseDate(from.getLastModified()));
    }
    return run;
  }

  public static InstrumentStatusDto asDto(InstrumentStatus from) {
    InstrumentStatusDto to = new InstrumentStatusDto();
    to.setInstrument(asDto(from.getInstrument()));
    to.setRun(from.getRun() == null ? null : asDto(from.getRun()));
    to.setPools(from.getRun() == null ? Collections.emptyList()
        : from.getRun().getSequencerPartitionContainers().stream()//
            .flatMap(c -> c.getPartitions().stream())//
            .map(Partition::getPool)
            .filter(Objects::nonNull)//
            .collect(Collectors.groupingBy(Pool::getId)).values().stream()//
            .map(l -> l.get(0))//
            .sorted((a, b) -> a.getAlias().compareTo(b.getAlias()))//
            .map(p -> asDto(p, false))//
            .collect(Collectors.toList()));
    return to;
  }

  public static SpreadsheetFormatDto asDto(SpreadSheetFormat from) {
    SpreadsheetFormatDto dto = new SpreadsheetFormatDto();
    dto.setName(from.name());
    dto.setDescription(from.description());
    return dto;
  }

  public static SpreadsheetDto asDto(Spreadsheet<?> from) {
    SpreadsheetDto dto = new SpreadsheetDto();
    dto.setDescription(from.description());
    dto.setName(from.name());
    return dto;
  }

  public static DeletionDto asDto(Deletion from) {
    DeletionDto dto = new DeletionDto();
    dto.setId(from.getId());
    dto.setTargetType(from.getTargetType());
    dto.setTargetId(from.getTargetId());
    dto.setDescription(from.getDescription());
    dto.setUserName(from.getUser().getFullName());
    dto.setChangeTime(formatDateTime(from.getChangeTime()));
    return dto;
  }

  public static BarcodableDto asDto(BarcodableView from) {
    BarcodableDto dto = new BarcodableDto();
    dto.setId(from.getId().getTargetId());
    dto.setEntityType(from.getId().getTargetType().toString());
    dto.setAlias(from.getAlias());
    dto.setName(from.getName());
    dto.setIdentificationBarcode(from.getIdentificationBarcode());
    return dto;
  }

  public static LibraryTemplateDto asDto(LibraryTemplate from) {
    LibraryTemplateDto dto = null;
    if (from instanceof DetailedLibraryTemplate) {
      dto = asDetailedLibraryTemplateDto((DetailedLibraryTemplate) from);
    } else {
      dto = new LibraryTemplateDto();
    }

    dto.setId(from.getId());
    dto.setAlias(from.getAlias());
    dto.setProjectId(from.getProject().getId());
    if (from.getPlatformType() != null) {
      dto.setPlatformType(from.getPlatformType().getKey());
    }
    if (from.getLibraryType() != null) {
      dto.setLibraryTypeId(from.getLibraryType().getId());
    }
    if (from.getLibrarySelectionType() != null) {
      dto.setSelectionTypeId(from.getLibrarySelectionType().getId());
    }
    if (from.getLibraryStrategyType() != null) {
      dto.setStrategyTypeId(from.getLibraryStrategyType().getId());
    }
    if (from.getKitDescriptor() != null) {
      dto.setKitDescriptorId(from.getKitDescriptor().getId());
    }
    if (from.getIndexFamily() != null) {
      dto.setIndexFamilyId(from.getIndexFamily().getId());
      if (from.getIndexOnes() != null) {
        dto.setIndexOneIds(from.getIndexOnes().entrySet().stream()
            .collect(Collectors.toMap(Entry::getKey, entry -> entry.getValue().getId())));
      }
      if (from.getIndexTwos() != null) {
        dto.setIndexTwoIds(from.getIndexTwos().entrySet().stream()
            .collect(Collectors.toMap(Entry::getKey, entry -> entry.getValue().getId())));
      }
    }
    return dto;
  }

  private static DetailedLibraryTemplateDto asDetailedLibraryTemplateDto(DetailedLibraryTemplate from) {
    DetailedLibraryTemplateDto dto = new DetailedLibraryTemplateDto();
    if (from.getLibraryDesign() != null) {
      dto.setDesignId(from.getLibraryDesign().getId());
    }
    if (from.getLibraryDesignCode() != null) {
      dto.setLibraryDesignCodeId(from.getLibraryDesignCode().getId());
    }
    return dto;
  }

  public static List<LibraryTemplateDto> asLibraryTemplateDtos(Collection<LibraryTemplate> from) {
    return from.stream().map(Dtos::asDto).collect(Collectors.toList());
  }

  public static WorkflowNameDto asDto(WorkflowName from) {
    WorkflowNameDto dto = new WorkflowNameDto();
    dto.setWorkflowName(from);
    dto.setDescription(from.getDescription());
    return dto;
  }

  public static WorkflowStateDto asDto(Workflow from) {
    WorkflowStateDto dto = new WorkflowStateDto();
    dto.setName(from.getProgress().getWorkflowName().getDescription());
    dto.setWorkflowId(from.getProgress().getId());
    dto.setLog(from.getLog());
    dto.setComplete(from.isComplete());
    dto.setLastModified(formatDateTime(from.getProgress().getLastModified()));
    if (from.isComplete()) {
      dto.setMessage(from.getConfirmMessage());
    } else {
      dto.setStepNumber(from.getNextStepNumber());
      WorkflowStepPrompt prompt = from.getStep(dto.getStepNumber());
      dto.setMessage(prompt.getMessage());
      dto.setInputTypes(prompt.getInputTypes());
    }
    return dto;
  }

  public static WorkflowStateDto asDto(Workflow from, int stepNumber) {
    WorkflowStateDto dto = new WorkflowStateDto();
    dto.setName(from.getProgress().getWorkflowName().getDescription());
    dto.setWorkflowId(from.getProgress().getId());
    dto.setLog(from.getLog());
    dto.setComplete(from.isComplete());
    dto.setLastModified(formatDateTime(from.getProgress().getLastModified()));
    if (stepNumber >= from.getLog().size()) {
      if (from.isComplete()) {
        dto.setMessage(from.getConfirmMessage());
      } else {
        dto.setStepNumber(from.getNextStepNumber());
      }
    } else {
      dto.setStepNumber(stepNumber);
    }
    if (dto.getStepNumber() != null) {
      WorkflowStepPrompt prompt = from.getStep(stepNumber);
      dto.setMessage(prompt.getMessage());
      dto.setInputTypes(prompt.getInputTypes());
    }
    return dto;
  }

  public static StorageLocationDto asDto(StorageLocation from, boolean includeChildLocations, boolean recursive) {
    StorageLocationDto dto = new StorageLocationDto();
    dto.setId(from.getId());
    if (from.getParentLocation() != null) {
      dto.setParentLocationId(from.getParentLocation().getId());
    }
    dto.setLocationUnit(from.getLocationUnit().name());
    switch (from.getLocationUnit().getBoxStorageAmount()) {
    case NONE:
      dto.setAvailableStorage(false);
      break;
    case SINGLE:
      dto.setAvailableStorage(from.getBoxes().isEmpty());
      break;
    case MULTIPLE:
      dto.setAvailableStorage(true);
      break;
    default:
      throw new IllegalStateException("Unexpected BoxStorageAmount");
    }
    dto.setAlias(from.getAlias());
    dto.setIdentificationBarcode(from.getIdentificationBarcode());
    dto.setDisplayLocation(from.getDisplayLocation());
    dto.setFullDisplayLocation(from.getFullDisplayLocation());
    if (includeChildLocations) {
      dto.setChildLocations(from.getChildLocations().stream()
          .map(child -> Dtos.asDto(child, recursive, recursive))
          .collect(Collectors.toList()));
    }
    dto.setBoxes(from.getBoxes().stream().map(box -> asDto(box, true)).collect(Collectors.toSet()));
    return dto;
  }

  public static StorageLocation to(StorageLocationDto from) {
    StorageLocation location = new StorageLocation();
    location.setId(from.getId());
    location.setAlias(from.getAlias());
    if (!LimsUtils.isStringEmptyOrNull(from.getIdentificationBarcode())) {
      location.setIdentificationBarcode(from.getIdentificationBarcode());
    }
    if (from.getParentLocationId() != null) {
      location.setParentLocation(new StorageLocation());
      location.getParentLocation().setId(from.getParentLocationId());
    }
    location.setLocationUnit(LocationUnit.valueOf(from.getLocationUnit()));
    return location;
  }
}
