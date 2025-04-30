-- ############## action_type ##############
select count(*) from action_type;

-- ############## activities ##############
select count(*) from activities;
select * from activities limit 10;

-- ############## activity_properties ##############
select count(*) from activity_properties;
select * from activity_properties limit 10;

-- ############## activity_smid ##############
select count(*) from activity_smid;
select * from activity_smid limit 10;

-- ############## activity_stds_lookup ##############
select count(*) from activity_stds_lookup;
select * from activity_stds_lookup limit 10;

-- ############## activity_supp ##############
select count(*) from activity_supp;
select * from activity_supp limit 10;

-- ############## activity_supp_map ##############
select count(*) from activity_supp_map;
select * from activity_supp_map limit 10;

-- ############## assay_class_map ##############
select count(*) from assay_class_map;
select * from assay_class_map limit 10;

-- ############## assay_classification ##############
select count(*) from assay_classification;
select * from assay_classification limit 10;

-- ############## assay_parameters ##############
select count(*) from assay_parameters;
select * from assay_parameters limit 10;

-- ############## assay_type ##############
select count(*) from assay_type;
select * from assay_type limit 10;

-- ############## assays ##############
select count(*) from assays;
select * from assays limit 10;
select assay_organism, count(*) from assays group by assay_organism;

-- ############## atc_classification ##############
select count(*) from atc_classification;
select * from atc_classification limit 10;

-- ############## binding_sites ##############
select count(*) from binding_sites;
select * from binding_sites limit 10;

-- ############## bio_component_sequences ##############
select count(*) from bio_component_sequences;
select * from bio_component_sequences limit 10;
select component_type, count(*) from bio_component_sequences group by component_type;

-- ############## bioassay_ontology ##############
select count(*) from bioassay_ontology;
select * from bioassay_ontology limit 10;

-- ############## biotherapeutic_components ##############
select count(*) from biotherapeutic_components;
select * from biotherapeutic_components limit 10;

-- ############## biotherapeutics ##############
select count(*) from biotherapeutics;
select * from biotherapeutics limit 10;

-- ############## cell_dictionary ##############
select count(*) from cell_dictionary;
select * from cell_dictionary limit 10;
select cell_source_organism, count(*) from cell_dictionary group by cell_source_organism;

-- ############## chembl_id_lookup ##############
select count(*) from chembl_id_lookup;
select * from chembl_id_lookup limit 10;
select entity_type, count(*) from chembl_id_lookup group by entity_type;

-- ############## chembl_release ##############
select count(*) from chembl_release;
select * from chembl_release limit 10;

-- ############## component_class ##############
select count(*) from component_class;
select * from component_class limit 10;

-- ############## component_domains ##############
select count(*) from component_domains;
select * from component_domains limit 10;

-- ############## component_go ##############
select count(*) from component_go;
select * from component_go limit 10;

-- ############## component_sequences ##############
select count(*) from component_sequences;
select * from component_sequences limit 10;
select component_type, count(*) from component_sequences group by component_type;

-- ############## component_synonyms ##############
select count(*) from component_synonyms;
select * from component_synonyms limit 10;

-- ############## compound_properties ##############
select count(*) from compound_properties;
select * from compound_properties limit 10;

-- ############## compound_records ##############
select count(*) from compound_records;
select * from compound_records limit 10;

-- ############## compound_structural_alerts ##############
select count(*) from compound_structural_alerts;
select * from compound_structural_alerts limit 10;

-- ############## other ##############
select count(*) from compound_structures;
select count(*) from confidence_score_lookup;
select count(*) from curation_lookup;
select count(*) from data_validity_lookup;
select count(*) from defined_daily_dose;
select count(*) from docs;
select count(*) from domains;
select count(*) from drug_indication;
select count(*) from drug_mechanism;
select count(*) from drug_warning;
select count(*) from formulations;
select count(*) from frac_classification;
select count(*) from go_classification;
select count(*) from hrac_classification;
select count(*) from indication_refs;
select count(*) from irac_classification;
select count(*) from ligand_eff;
select count(*) from mechanism_refs;
select count(*) from metabolism;
select count(*) from metabolism_refs;
select count(*) from molecule_atc_classification;
select count(*) from molecule_dictionary;
select count(*) from molecule_frac_classification;
select count(*) from molecule_hierarchy;
select count(*) from molecule_hrac_classification;
select count(*) from molecule_irac_classification;
select count(*) from molecule_synonyms;
select count(*) from organism_class;
select count(*) from patent_use_codes;
select count(*) from predicted_binding_domains;
select count(*) from product_patents;
select count(*) from products;
select count(*) from protein_class_synonyms;
select count(*) from protein_classification;
select count(*) from relationship_type;
select count(*) from research_companies;
select count(*) from research_stem;
select count(*) from site_components;
select count(*) from source;
select count(*) from structural_alert_sets;
select count(*) from structural_alerts;
select count(*) from target_components;
select count(*) from target_dictionary;
select count(*) from target_relations;
select count(*) from target_type;
select count(*) from tissue_dictionary;
select count(*) from usan_stems;
select count(*) from variant_sequences;
select count(*) from version;
select count(*) from warning_refs;
