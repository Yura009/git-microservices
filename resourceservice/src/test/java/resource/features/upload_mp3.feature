Feature: Upload MP3 file and notify processor

  Scenario: A valid MP3 file is uploaded successfully
    Given a valid MP3 file
    When the client uploads the file
    Then the file is saved in cloud storage
    And metadata is saved in the database
