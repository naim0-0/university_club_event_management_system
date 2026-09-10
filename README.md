# Campus Event Management System

A Java 17 + JavaFX desktop application for creating club events, moving them through an approval workflow, and registering attendees with role-based pricing. Data is stored in the existing SQLite database.

## Demo login accounts

| Role | Email | Password |
|---|---|---|
| Administrator | `admin@campus.edu` | `admin123` |
| Club president | `rahim@campus.edu` | `president123` |
| Student | `karim@campus.edu` | `student123` |
| Student | `ayesha@campus.edu` | `student123` |
| Student | `nusrat@campus.edu` | `student123` |
| Student | `tanvir@campus.edu` | `student123` |
| Student | `mehedi@campus.edu` | `student123` |

Passwords are stored as salted PBKDF2 hashes. Change these demonstration passwords before using the application with real data.

Seeded clubs include Robotics Club, IITSEC, and IITDU CTF.

## Run

```bash
mvn clean javafx:run
```

The application initializes missing tables and adds a small sample data set only when the user table is empty.

## Architecture

The application follows a layered flow:

`JavaFX View → MainController → Services/Facade → DAOs → SQLite`

- `ui`: constructs the JavaFX view and exposes controls; it contains no business rules.
- `controller`: translates user gestures into service calls and refreshes view data.
- `security`: hashes passwords and enforces role permissions in backend workflows.
- `service`: owns use cases, validation, workflow transitions, and error translation.
- `dao`: contains SQL and maps result rows to domain objects.
- `models`: simple domain entities.
- `validation` and the existing pattern packages: reusable backend policies.

## Design patterns used

| Pattern | Where used | Project problem solved | Important classes |
|---|---|---|---|
| Singleton | Database infrastructure | The desktop app needs one shared SQLite connection instead of each DAO opening an unrelated connection. | `DatabaseConnection`, all DAOs |
| Builder | Event creation | Event construction has several named fields and a default draft status, so services do not depend on constructor ordering. Business validation remains in its own pipeline. | `EventBuilder`, `Event`, `EventService` |
| Strategy | Registration pricing | Regular, early-bird, and club-president discounts vary independently from registration orchestration. | `PricingStrategy`, three concrete strategies, `FeeCalculator` |
| State | Event approval workflow | Submit, approve, and reject are legal only in particular lifecycle states. State objects enforce transitions without controller conditionals. | `EventState`, concrete states, `EventContext`, `EventStateFactory` |
| Observer | Activity notifications | Event creation and state changes should notify interested listeners without coupling the service to JavaFX. | `EventObserver`, `EventNotifier`, `EventService` |
| Facade | Attendee registration | Registration requires user lookup, event eligibility, duplicate checking, pricing, and persistence. One operation hides that subsystem from the controller. | `RegistrationFacade`, DAOs, `FeeCalculator` |
| Chain of Responsibility | Event validation | Title, budget, and club rules should be independently understandable and extendable while running as one validation pipeline. | `ValidationHandler`, concrete validators, `EventValidationChain` |

## Patterns intentionally not used

- Abstract Factory and Factory Method: there is no family of related products or varying construction subsystem.
- Prototype: the application does not clone expensive or highly configured objects.
- Adapter and Bridge: there is no incompatible external API or pair of independent implementation dimensions.
- Composite: clubs and events are flat records, not a part/whole tree.
- Decorator and Proxy: no optional runtime responsibilities, remote objects, access wrapper, or lazy-loading requirement exists.
- Flyweight: the data volume is small and sharing intrinsic object state would add complexity without saving meaningful memory.
- Command and Memento: the current workflows do not require queues or undo/redo.
- Interpreter: no domain language or expression grammar is present.
- Iterator: Java collections already provide the required traversal.
- Mediator: the small service graph is already coordinated by the registration facade.
- Template Method: there are no multiple processes sharing a stable algorithm skeleton.
- Visitor: the model structure is small and changes more often than potential cross-cutting operations.

## Main workflows

1. Create an event as `DRAFT` after validation.
2. Submit a draft (or resubmit a rejected event) to `PENDING`.
3. Approve or reject a pending event.
4. Register a user only for an approved event. Presidents receive 20% off; other users can receive the 10% early-bird discount.
5. Review persisted registrations and in-session workflow activity.

## Role permissions

- Administrators can create events, submit them, approve/reject them, and register any user.
- Club presidents can create and submit events only for a club they lead. They cannot approve or reject events and can register only themselves.
- Students cannot create or approve events; they can browse approved events and register themselves.
