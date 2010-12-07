public class AntMapping {
  def project_file = 'build.xml'
  
  def phase_mappings = [
    'CLEAN': 'clean',
    'PACKAGE': 'build',
    'VERIFY': 'dist'
  ]
  
  def project_defs = [:]
  
  def file = {fname -> project_file = fname}
  def mappings = {phase_map ->
    phase_map.each { entry ->
      phase_mappings[entry.key] = entry.value
    }
  }
  
  def projects = { project_metadata_map ->
  }
}