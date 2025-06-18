package amu.eJury.service.api;

import amu.eJury.model.result.ExceptionalStatus;

/**
 * Gestion des notes (ou statuts) d’un étudiant pour une UE.
 */
public interface GradeService {

    /**
     * Crée ou met à jour la note/statut d’un étudiant dans une UE.
     *
     * @param ueId        identifiant de la TeachingUnit
     * @param studentId   identifiant de l’étudiant
     * @param numeric     note sur 20 ; peut être {@code null} si statut exceptionnel
     * @param status      NONE, ABI, ABJ ou AR
     */
    void saveOrUpdate(long ueId,
                      long studentId,
                      Double numeric,
                      ExceptionalStatus status);
}
