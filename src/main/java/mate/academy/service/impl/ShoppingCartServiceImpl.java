package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private TicketDao ticketDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket(movieSession, user);
        ticket = ticketDao.add(ticket);
        ShoppingCart userShoppingCart = getByUser(user);
        userShoppingCart.getTickets().add(ticket);
        shoppingCartDao.update(userShoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(() ->
                new RuntimeException("Can't get shopping cart for user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(user);
        shoppingCartDao.add(newShoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        List<Ticket> newTicketList = new ArrayList<>();
        shoppingCart.setTickets(newTicketList);
        shoppingCartDao.update(shoppingCart);
    }
}