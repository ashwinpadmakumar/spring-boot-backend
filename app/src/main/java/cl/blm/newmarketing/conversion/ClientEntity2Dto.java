package cl.blm.newmarketing.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.model.entities.Client;
import cl.blm.newmarketing.rest.dtos.ClientDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ClientEntity2Dto
    implements Converter<Client, ClientDto> {
  @Override
  public ClientDto convert(Client source) {
    ClientDto target = new ClientDto();
    target.setClientId(source.getId());
    // TODO can bind person too?
    return target;
  }
}
