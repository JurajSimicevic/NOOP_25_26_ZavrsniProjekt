package BackEnd;

import ObserversAndOtherComps.ViewPanel;

/**
 * Enumeracija koja definira ključne tipove entiteta unutar sustava.
 * <p>
 * Služi kao centralni klasifikator za prepoznavanje skupova podataka i usmjeravanje
 * logike unutar <b>DAO sloja</b>, tvornica (<i>Factories</i>) i grafičkog sučelja.
 * </p>
 * <b>Primjena u sustavu:</b>
 * <ul>
 * <li><b>Učitavanje i spremanje:</b> Pomaže sustavu da odluči koju tablicu u bazi podataka treba dohvatiti.</li>
 * <li><b>Filtriranje:</b> Omogućuje dinamičku promjenu prikaza u {@link ViewPanel} klasama ovisno o odabranom tipu.</li>
 * <li><b>Tipizacija:</b> Osigurava <i>Type-safety</i> (sigurnost tipova) pri prosljeđivanju parametara u generičkim metodama.</li>
 * </ul>
 * <b>Definirane konstante:</b>
 * <ul>
 * <li>{@code BOOK}: Predstavlja katalog knjiga i njihov trenutni status.</li>
 * <li>{@code CUSTOMER}: Predstavlja korisnike biblioteke i njihova zaduženja.</li>
 * <li>{@code LIBRARIAN}: Predstavlja entitete zaposlenika s administratorskim pravima.</li>
 * </ul>
 */
public enum FilePathEnum {

    KNJIGE,

    CUSTOMERS,

    LIBRARIANS
}
